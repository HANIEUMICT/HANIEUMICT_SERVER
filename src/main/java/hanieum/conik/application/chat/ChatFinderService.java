package hanieum.conik.application.chat;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.application.chat.required.*;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.entity.ChatRoom;
import hanieum.conik.domain.chat.entity.ChatRoomMember;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
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
        return chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorType.MEMBER_NOT_IN_CHAT_ROOM));
    }

    @Override
    public List<ChatRoomMember> findRoomMembers(Long roomId){
        return chatRoomMemberRepository.findByChatRoom_Id(roomId);
    }

    @Override
    public Page<ChatRoomSummary> findRoomsByMember(Long memberId, Pageable pageable) {
        Member member = memberFinder.findById(memberId);

        Page<ChatRoomMember> page = chatRoomMemberRepository.findByMemberId(member.getId(), pageable);
        if (page.isEmpty()) return Page.empty(pageable);

        List<ChatRoomMember> chatRoomMembers = page.getContent();

        List<Long> roomIds = chatRoomMembers.stream()
                .map(crm -> crm.getChatRoom().getId())
                .toList();

        LastMessageInfo info = getLastMessageInfo(memberId, roomIds);

        Map<Long, List<ChatRoomMember>> roomMembersMap = chatRoomMemberRepository.findByChatRoom_IdIn(roomIds)
                .stream()
                .collect(Collectors.groupingBy(crm -> crm.getChatRoom().getId()));

        List<ChatRoomSummary> summaries = chatRoomMembers.stream()
                .map(crm -> getRoomSummary(crm, info, roomMembersMap))
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

    private ChatRoomSummary getRoomSummary(
            ChatRoomMember crm,
            LastMessageInfo info,
            Map<Long, List<ChatRoomMember>> roomMembersMap
    ) {
        ChatRoom room = crm.getChatRoom();
        Long roomId = room.getId();

        List<ChatRoomMember> members = roomMembersMap.getOrDefault(roomId, List.of());
        String roomName = buildRoomNameForMember(room, crm.getMemberId(), members);

        ChatMessageDto last = info.lastMessageMap().get(roomId);

        if (last == null) {
            return ChatRoomSummary.of(room, roomName, 0, null);
        }

        long myLast = info.myLastReadMap().getOrDefault(roomId, 0L);
        long unread = Math.max(0L, last.seq() - myLast);

        return ChatRoomSummary.of(room, roomName, unread, last);
    }

    private String buildRoomNameForMember(ChatRoom room, Long myId, List<ChatRoomMember> members) {
        // memberId -> 표시 이름
        List<Long> memberIds = members.stream()
                .map(ChatRoomMember::getMemberId)
                .toList();
        Map<Long, String> idToName = memberFinder.findAllByIds(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Member::getName, (a, b) -> a));

        if (room.getType() == ChatRoomType.PRIVATE) {
            // 나 제외 1명
            return members.stream()
                    .map(ChatRoomMember::getMemberId)
                    .filter(id -> !id.equals(myId))
                    .findFirst()
                    .map(idToName::get)
                    .orElse("(알 수 없음)");
        }

        // GROUP: 나 제외 이름들로 "A, B, C 외 n명"
        List<String> others = members.stream()
                .map(ChatRoomMember::getMemberId)
                .filter(id -> !id.equals(myId))
                .map(idToName::get)
                .filter(n -> n != null && !n.isBlank())
                .distinct()
                .sorted()
                .toList();

        return formatGroupRoomName(others, 3);
    }

    private String formatGroupRoomName(List<String> names, int limit) {
        if (names == null || names.isEmpty()) return "그룹 채팅";

        List<String> shown = names.stream().limit(limit).toList();
        int remain = names.size() - shown.size();

        String base = String.join(", ", shown);
        return remain > 0 ? base + " 외 " + remain + "명" : base;
    }



    // 특정 메시지(seq) 이전의 N개 메시지를 가져온다.
    @Override
    public Slice<ChatMessageDto> fetchMessagesBeforeSeq(Long roomId, Long beforeSeq, int size, Long memberId) {
        // 멤버가 채팅방의 구성원인지 확인
        chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId)
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

        List<ChatMessageDto> content = rows.stream()
                .map(msg -> ChatMessageDto.fromEntity(msg, getSenderName(msg.getSenderId())))
                .toList();
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private String getSenderName(Long senderId) {
        Member sender = memberFinder.findById(senderId);
        return sender.getName();
    }

    @Override
    public boolean isRoomMember(Long roomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId).isPresent();
    }

    @Override
    public long findLatestMessageSeq(Long roomId) {
        return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId)
                .map(ChatMessage::getSeq)
                .orElse(0L);
    }
}