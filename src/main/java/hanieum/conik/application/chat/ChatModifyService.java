package hanieum.conik.application.chat;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.adapter.chat.dto.ChatMessageRequest;
import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.application.chat.provided.ChatSaver;
import hanieum.conik.application.chat.required.*;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.*;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatModifyService implements ChatSaver {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberFinder memberFinder;
    private final ChatFinder chatFinder;
    private final ChatMessageRepository chatMessageRepository; // Mongo

    private final StringRedisTemplate stringRedisTemplate;     // seq 발급용(INCR)
    private final SimpMessagingTemplate messagingTemplate;     // STOMP 브로드캐스트

    @Override
    public ChatMessage sendMessage(Long roomId, ChatMessageRequest request) {
        // 채팅방 및 발신자 검증
        ChatRoom chatRoom = chatFinder.findRoomByRoomId(request.roomId()); // ChatRoomSummary 생성을 위해 필요
        Member sender = memberFinder.findById(request.senderId());
        chatFinder.findChatRoomMember(request.roomId(), sender.getId()); // 멤버 유효성 검증

        // 채팅 메시지 저장
        long seq = nextSequence(request.roomId());
        ChatMessage chatMessage = ChatMessage.create(
                request.roomId(),
                request.senderId(),
                request.content(),
                request.type(),
                request.fileUrl(),
                request.fileName(),
                request.fileSize(),
                seq);
        chatMessage = chatMessageRepository.save(chatMessage);

        // 발신자 본인의 읽음 처리 업데이트
        updateLastRead(request.roomId(), request.senderId(), seq);

        // STOMP 채팅방으로 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/room/" + request.roomId(), toPayload(chatMessage, sender.getName()));

        // 채팅방 요약 정보 개인 토픽으로 전송
        List<ChatRoomMember> chatRoomMembers = chatFinder.findRoomMembers(request.roomId());
        Map<Long, String> roomNameMap = buildRoomNamePerMember(chatRoom, chatRoomMembers);

        for (ChatRoomMember m : chatRoomMembers) {
            Long memberId = m.getMember().getId();

            // 읽지 않은 메시지 수 계산
            long unread = calculateUnreadMessage(request, m, memberId, seq);
            String roomName = roomNameMap.getOrDefault(memberId, "채팅");

            // ChatRoomSummary 생성
            ChatMessageDto chatMessageDto = ChatMessageDto.fromEntity(chatMessage, sender.getName());
            messagingTemplate.convertAndSend("/topic/user." + memberId + ".room-summary", ChatRoomSummary.of(chatRoom, roomName, unread, chatMessageDto));
            log.info("📡 [convertAndSend] 개인 토픽 전송: /topic/user.{}.room-summary", memberId);
        }

        return chatMessage;
    }

    private static long calculateUnreadMessage(ChatMessageRequest request, ChatRoomMember m, Long memberId, long currentSeq) {
        long lastReadSeq = Optional.ofNullable(m.getLastReadSeq()).orElse(0L);
        long unread = memberId.equals(request.senderId()) ? 0L : Math.max(0, currentSeq - lastReadSeq);

        return unread;
    }

    private Map<Long, String> buildRoomNamePerMember(
            ChatRoom room,
            List<ChatRoomMember> members
    ) {
        // memberId -> 이름
        Map<Long, String> idToName = members.stream()
                .collect(Collectors.toMap(
                        m -> m.getMember().getId(),
                        m -> m.getMember().getName() // nickname이면 변경
                ));

        Map<Long, String> result = new HashMap<>();

        for (ChatRoomMember me : members) {
            Long myId = me.getMember().getId();

            if (room.getType() == ChatRoomType.PRIVATE) {
                // DM: 나 제외한 한 명
                String opponent = members.stream()
                        .map(m -> m.getMember().getId())
                        .filter(id -> !id.equals(myId))
                        .findFirst()
                        .map(idToName::get)
                        .orElse("(알 수 없음)");

                result.put(myId, opponent);
                continue;
            }

            // GROUP: 나 제외한 이름들
            List<String> others = members.stream()
                    .map(m -> m.getMember().getId())
                    .filter(id -> !id.equals(myId))
                    .map(idToName::get)
                    .filter(n -> n != null && !n.isBlank())
                    .distinct()
                    .sorted()
                    .toList();

            result.put(myId, formatGroupRoomName(others, 3));
        }
        return result;
    }

    private String formatGroupRoomName(List<String> names, int limit) {
        if (names == null || names.isEmpty()) {
            return "그룹 채팅";
        }

        List<String> shown = names.stream()
                .limit(limit)
                .toList();

        int remain = names.size() - shown.size();
        String base = String.join(", ", shown);

        return (remain > 0)
                ? base + " 외 " + remain + "명"
                : base;
    }


    @Override
    public Long createPrivateRoom(Long memberAId, Long memberBId) {
        // member 조회
        Member memberA = memberFinder.findById(memberAId);
        Member memberB = memberFinder.findById(memberBId);

        // 기존 1:1 채팅방 존재 여부 확인
        Optional<ChatRoom> chatRoom = chatRoomRepository.findPrivateRoomBetweenMembers(memberAId, memberBId, ChatRoomType.PRIVATE);
        if (chatRoom.isPresent()) {return chatRoom.get().getId();}

        // 채팅방 생성
        ChatRoom newChatRoom = ChatRoom.createWithMembers(ChatRoomType.PRIVATE, List.of(memberA, memberB));
        chatRoomRepository.save(newChatRoom);

        return newChatRoom.getId();
    }

    @Override
    public void leaveChatRoom(Long roomId, Long memberId) {
        // 채팅방 조회
        ChatRoomMember chatRoomMember = chatFinder.findChatRoomMember(roomId, memberId);
        chatRoomMemberRepository.delete(chatRoomMember);

        long remain = chatRoomMemberRepository.countByChatRoom_Id(roomId);

        if (remain == 0) {
            long deleted = chatMessageRepository.deleteByRoomId(roomId);
            log.info("[CHAT] deleted messages for room {} = {}", roomId, deleted);

            chatRoomRepository.deleteById(roomId);
        }
    }

    // 특정 사용자가 방에서 어디까지 읽었는지 기록하는 읽음 처리 API
    @Override
    public void updateLastRead(Long roomId, Long memberId, long lastReadSeq) {
        ChatRoomMember chatRoomMember = chatFinder.findChatRoomMember(roomId, memberId);
        if (chatRoomMember.getLastReadSeq() == null || lastReadSeq > chatRoomMember.getLastReadSeq()) {
            chatRoomMember.updateLastRead(lastReadSeq);
        }
    }

    private Map<String, Object> toPayload(ChatMessage m, String senderName) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("roomId",   m.getRoomId());                 // 반드시 값 있음
        payload.put("senderId", m.getSenderId());               // 반드시 값 있음
        payload.put("type",     m.getType().name());     // enum은 name()로 문자열 전송 권장
        payload.put("senderName", senderName);
        payload.put("seq",      m.getSeq());
        payload.put("createdAt", m.getCreatedAt());

        // TEXT/SYSTEM일 때만 content
        if (m.getContent() != null && !m.getContent().isBlank()) {
            payload.put("content", m.getContent());
        }
        // IMAGE/VIDEO/FILE일 때만 파일 정보
        if (m.getFileUrl() != null && !m.getFileUrl().isBlank()) {
            payload.put("fileUrl", m.getFileUrl());
        }
        if (m.getFileName() != null && !m.getFileName().isBlank()) {
            payload.put("fileName", m.getFileName());
        }
        if (m.getFileSize() != null) {
            payload.put("fileSize", m.getFileSize());
        }

        return payload;
    }

    private Long nextSequence(Long roomId) {
        String key = "chat:seq:" + roomId;
        return stringRedisTemplate.opsForValue().increment(key);
    }
}
