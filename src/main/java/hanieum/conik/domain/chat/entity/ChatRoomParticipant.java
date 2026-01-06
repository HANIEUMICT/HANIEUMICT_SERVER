package hanieum.conik.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("chat_room_participant")
public class ChatRoomParticipant implements Serializable {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    @Indexed
    private Long memberId;

    private String serverId;

    private String sessionId;

    private Instant enteredAt;

    private Instant lastActiveAt;

    /* ===================== 생성 ===================== */

    public static ChatRoomParticipant enter(
            Long roomId,
            Long memberId,
            String serverId,
            String sessionId
    ) {
        Instant now = Instant.now();
        return ChatRoomParticipant.builder()
                .id(roomId + ":" + memberId)
                .roomId(roomId)
                .memberId(memberId)
                .serverId(serverId)
                .sessionId(sessionId)
                .enteredAt(now)
                .lastActiveAt(now)
                .build();
    }

    /* ===================== 상태 갱신 ===================== */

    /** 메시지 전송 / heartbeat / activity 시 호출 */
    public void refreshActivity() {
        this.lastActiveAt = Instant.now();
    }

    /* ===================== 온라인 판단 ===================== */

    /**
     * onlineThresholdSeconds 예: 30초
     * now - lastActiveAt <= threshold → 온라인
     */
    public boolean isOnline(long onlineThresholdSeconds) {
        return lastActiveAt != null &&
                lastActiveAt.isAfter(Instant.now().minusSeconds(onlineThresholdSeconds));
    }
}
