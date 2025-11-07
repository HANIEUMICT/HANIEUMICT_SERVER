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
@RedisHash(value = "chat_room_participant", timeToLive = 300)
public class ChatRoomParticipant implements Serializable {
    @Id
    private String id;

    @Indexed
    private String roomId;

    @Indexed
    private String memberId;

    private Instant enteredAt;    // 입장 시각
    private Instant lastActiveAt; // 마지막 활동 시각
    private Long lastReadSeq;     // 마지막 읽은 메시지 번호

    /** 입장 처리 */
    public static ChatRoomParticipant enter(String roomId, String memberId) {
        return ChatRoomParticipant.builder()
                .id(roomId + ":" + memberId)
                .roomId(roomId)
                .memberId(memberId)
                .enteredAt(Instant.now())
                .lastActiveAt(Instant.now())
                .lastReadSeq(0L)
                .build();
    }

    /** 활동 시간 갱신 */
    public void refreshActivity() {
        this.lastActiveAt = Instant.now();
    }

    /** 메시지 읽음 위치 갱신 */
    public void updateLastRead(Long seq) {
        if (seq != null && seq > (this.lastReadSeq == null ? 0 : this.lastReadSeq)) {
            this.lastReadSeq = seq;
        }
    }
}
