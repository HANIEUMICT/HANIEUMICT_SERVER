package hanieum.conik.domain.chat.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(
        name = "chat_room_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_room_id", "member_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember extends AbstractEntity {
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "chat_room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long lastReadSeq = 0L;

    @Column(nullable = true)
    private Instant lastReadAt;

    public void updateLastRead(long seq) {
        if (seq > (lastReadSeq == null ? 0 : lastReadSeq)) {
            lastReadSeq = seq;
            lastReadAt = Instant.now();
        }
    }

    // 변경됨: 생성자에서 Member 객체 대신 memberId를 직접 받음
    private ChatRoomMember(ChatRoom chatRoom, Long memberId) {
        this.chatRoom = chatRoom;
        this.memberId = memberId;
    }

    // 변경됨: 팩토리 메서드도 ID를 받도록 수정
    public static ChatRoomMember create(ChatRoom chatRoom, Long memberId) {
        return new ChatRoomMember(chatRoom, memberId);
    }
}
