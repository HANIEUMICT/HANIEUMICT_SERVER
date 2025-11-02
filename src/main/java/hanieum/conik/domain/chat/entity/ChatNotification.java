package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.chat.enumerate.ChatNotificationStatus;
import hanieum.conik.domain.member.Member;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatNotification extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver; // 알림 수신자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom; // 어떤 방에서 발생한 알림인지

    @Column(nullable = false)
    private String content; // 알림 내용 ("A님이 메시지를 보냈습니다.")

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatNotificationStatus status; // SENT, READ, DELETED 등

    /** 알림 읽음 처리 */
    public void markAsRead() {
        this.status = ChatNotificationStatus.READ;
    }

    /** 알림 삭제 처리 */
    public void delete() {
        this.status = ChatNotificationStatus.DELETED;
    }

    /** 알림이 읽히지 않은 상태인지 */
    public boolean isUnread() {
        return this.status == ChatNotificationStatus.SENT;
    }
}
