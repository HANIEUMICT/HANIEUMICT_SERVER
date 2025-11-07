package hanieum.conik.application.chat.provided;

import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatMessage;
public interface ChatSaver {
    /** 메시지 전송: seq 발급 → Mongo 저장 → STOMP 브로드캐스트 → 알림 생성 */
    ChatMessage sendMessage(ChatMessageDto request);

    /** 1:1 채팅방 생성(중복 방 방지 규칙 내장) */
    Long createPrivateRoom(Long memberAId, Long memberBId);

    /** 채팅방 나가기 */
    void leaveChatRoom(Long roomId, Long memberId);

    /** 읽음 처리 */
    void updateLastRead(Long roomId, Long memberId, long lastReadSeq);
}
