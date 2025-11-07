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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatFinderService implements ChatFinder {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberFinder memberFinder;

    private final ChatMessageRepository chatMessageRepository;  // Mongo

    private final StringRedisTemplate stringRedisTemplate; // seq 발급용(INCR)

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
    public long fetchCurrentRoomLatestSeq(Long roomId){
        return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId).map(ChatMessage::getSeq).orElse(0L);
    }

    @Override
    public Page<ChatRoomSummary> findRoomsByMember(Long memberId, Pageable pageable) {
        Member member = memberFinder.findById(memberId);
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByMember_Id(member.getId());
        if(chatRoomMembers.isEmpty()) return Page.empty(pageable);

        // member가 속해있는 채팅방들
        List<ChatRoom> chatRooms = chatRoomMembers.stream()
                .map(ChatRoomMember::getChatRoom)
                .toList();

        // 방 ID 목록
        List<Long> roomIds = chatRooms.stream()
                .map(ChatRoom::getId)
                .toList();

        // 1) Mongo에서 방별 최신 seq를 한 번에 조회
        Map<Long, Long> latestSeqMap = chatMessageRepository.findMaxSeqForRoomIds(roomIds);

        // 2) RDB(ChatRoomMember)에서 내 lastReadSeq를 한 번에 조회
        Map<Long, Long> myLastReadMap = chatRoomMemberRepository.findLastReadSeqs(memberId, roomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatRoomMemberRepository.LastReadView::getRoomId,
                        v -> Optional.ofNullable(v.getLastReadSeq()).orElse(0L)
                ));

        // 3) 각 방별로 요약 정보 생성
        List<ChatRoomSummary> summaries = chatRooms.stream()
                .map(room -> {
                    long latest = Optional.ofNullable(latestSeqMap.get(room.getId())).orElse(0L);
                    long myLast = Optional.ofNullable(myLastReadMap.get(room.getId())).orElse(0L);
                    long unread = Math.max(0L, latest - myLast);
                    ChatMessageDto last = findLastMessage(room.getId()).orElse(null);
                    return ChatRoomSummary.of(room, unread, last);
                })
                .toList();

        return new PageImpl<>(summaries, pageable, chatRoomMembers.size());
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

        // 화면 노출은 보통 오래된→최신 순이 편하므로 ASC로 뒤집기
        Collections.reverse(rows);

        List<ChatMessageDto> content = rows.stream().map(ChatMessageDto::fromEntity).toList();
        return new SliceImpl<>(content, PageRequest.of(0, size), hasNext);
    }

    @Override
    public boolean isRoomMember(Long roomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(roomId, memberId).isPresent();
    }

    @Override
    public long findLatestMessageSeq(Long roomId) {
        long currentSeq = getCurrentSeq(roomId); // 이미 서비스에 있음
        if (currentSeq > 0) return currentSeq;

        return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId)
                .map(ChatMessage::getSeq)
                .orElse(0L);
    }

    private long getCurrentSeq(Long roomId) {
        String seqStr = stringRedisTemplate.opsForValue().get("chat:seq:" + roomId);
        if (seqStr == null) return 0L;
        try { return Long.parseLong(seqStr); } catch (NumberFormatException e) { return 0L; }
    }

    private Optional<ChatMessageDto> findLastMessage(Long roomId) {
        return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId)
                .map(ChatMessageDto::fromEntity);
    }
}
