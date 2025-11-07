package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.member.Member;
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

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

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

    private ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }

    public static ChatRoomMember create(ChatRoom chatRoom, Member member) {
        return new ChatRoomMember(chatRoom, member);
    }
}
