package hanieum.conik.application.chat;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.application.chat.required.*;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.entity.ChatRoom;
import hanieum.conik.domain.chat.entity.ChatRoomMember;
import hanieum.conik.domain.chat.exception.ChatErrorType;
import hanieum.conik.domain.chat.exception.ChatException;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFinderService implements ChatFinder {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberFinder memberFinder;

    private final ChatMessageRepository chatMessageRepository;  // Mongo

    @Override
    public ChatRoom findRoomByRoomId(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorType.CHAT_ROOM_NOT_FOUND));
    }

    @Override
    public ChatRoomMember findChatRoomMember(Long roomId, Long memberId){
        return chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(roomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorType.MEMBER_NOT_IN_CHAT_ROOM));
    }

    @Override
    public List<ChatRoomMember> findRoomMembers(Long roomId){
        return chatRoomMemberRepository.findByChatRoom_Id(roomId);
    }

    @Override
    public Page<ChatRoomSummary> findRoomsByMember(Long memberId, Pageable pageable) {
        Member member = memberFinder.findById(memberId);

        Page<ChatRoomMember> page = chatRoomMemberRepository.findByMember_Id(member.getId(), pageable);
        if (page.isEmpty()) return Page.empty(pageable);

        List<ChatRoomMember> chatRoomMembers = page.getContent();

        List<Long> roomIds = chatRoomMembers.stream()
                .map(crm -> crm.getChatRoom().getId())
                .toList();

        LastMessageInfo info = getLastMessageInfo(memberId, roomIds);

        List<ChatRoomSummary> summaries = chatRoomMembers.stream()
                .map(crm -> getRoomSummary(crm, info))
                .toList();


        return new PageImpl<>(summaries, pageable, page.getTotalElements());
    }

    private LastMessageInfo getLastMessageInfo(Long memberId, List<Long> roomIds) {
        Map<Long, ChatMessageDto> lastMessageMap = chatMessageRepository.findLastMessagesForRooms(roomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatMessageDto::roomId,
                        dto -> dto,
                        (a, b) -> a
                ));

        Map<Long, Long> myLastReadMap = chatRoomMemberRepository.findLastReadSeqs(memberId, roomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatRoomMemberRepository.LastReadView::getRoomId,
                        v -> Optional.ofNullable(v.getLastReadSeq()).orElse(0L)
                ));

        return new LastMessageInfo(lastMessageMap, myLastReadMap);
    }

    private record LastMessageInfo(
            Map<Long, ChatMessageDto> lastMessageMap,
            Map<Long, Long> myLastReadMap
    ) {}

    private ChatRoomSummary getRoomSummary(ChatRoomMember crm, LastMessageInfo info) {
        Long roomId = crm.getChatRoom().getId();
        ChatMessageDto last = info.lastMessageMap().get(roomId);

        // 마지막 메시지가 없으면 unread는 0
        if (last == null) {
            return ChatRoomSummary.of(crm.getChatRoom(), 0, null);
        }

        long myLast = info.myLastReadMap().getOrDefault(roomId, 0L);
        long unread = Math.max(0L, last.seq() - myLast);

        return ChatRoomSummary.of(crm.getChatRoom(), unread, last);
    }

    // 특정 메시지(seq) 이전의 N개 메시지를 가져온다.
    @Override
    public Slice<ChatMessageDto> fetchMessagesBeforeSeq(Long roomId, Long beforeSeq, int size, Long memberId) {
        // 멤버가 채팅방의 구성원인지 확인
        chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(roomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorType.MEMBER_NOT_IN_CHAT_ROOM));

        Pageable pageable = PageRequest.of(0, size + 1, Sort.by(DESC, "seq"));
        List<ChatMessage> rows;

        if (beforeSeq == null) {
            // 첫 로딩: 최신부터 size개
            rows = chatMessageRepository.findByRoomIdOrderBySeqDesc(roomId, pageable);
        } else {
            // 이전 페이지: beforeSeq 미만
            rows = chatMessageRepository.findByRoomIdAndSeqLessThanOrderBySeqDesc(roomId, beforeSeq, pageable);
        }

        boolean hasNext = rows.size() > size;
        if (hasNext) rows = rows.subList(0, size);

        Collections.reverse(rows);

        List<ChatMessageDto> content = rows.stream().map(ChatMessageDto::fromEntity).toList();
        return new SliceImpl<>(content, pageable, hasNext);
    }

        @Override
        public boolean isRoomMember(Long roomId, Long memberId) {
            return chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(roomId, memberId).isPresent();
        }

        @Override
        public long findLatestMessageSeq(Long roomId) {
            return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId)
                    .map(ChatMessage::getSeq)
                    .orElse(0L);
        }
}