package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import hanieum.conik.domain.chat.exception.ChatErrorType;
import hanieum.conik.domain.chat.exception.ChatException;
import hanieum.conik.domain.member.Member;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends AbstractEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    private ChatRoom(ChatRoomType chatRoomType) {
        this.type = chatRoomType;
    }

    public static ChatRoom create(ChatRoomType type) {
        return new ChatRoom(type);
    }

    public static ChatRoom createWithMembers(ChatRoomType type, Collection<Member> members) {
        ChatRoom room = new ChatRoom(type);
        if (members != null) members.forEach(room::addMember);
        return room;
    }

    public void addMember(Member member) {
        boolean exists = chatRoomMembers.stream()
                .anyMatch(m -> m.getMemberId().equals(member.getId()));
        if (exists) throw new ChatException(ChatErrorType.ALREADY_IN_CHAT_ROOM);

        chatRoomMembers.add(ChatRoomMember.create(this, member.getId()));
    }

    public void removeMember(Member member) {
        chatRoomMembers.removeIf(m -> m.getMemberId().equals(member.getId()));
    }
}