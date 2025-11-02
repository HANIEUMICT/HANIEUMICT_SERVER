package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.member.Member;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember extends AbstractEntity {
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    public ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }
}
