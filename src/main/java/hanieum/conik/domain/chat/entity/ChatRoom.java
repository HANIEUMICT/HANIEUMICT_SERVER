package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.chat.dto.ChatRoomCreateRequest;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import hanieum.conik.domain.member.Member;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends AbstractEntity {
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    private ChatRoom(String title, ChatRoomType chatRoomType, List<ChatRoomMember> chatRoomMembers) {
        this.title = title;
        this.type = chatRoomType;
        this.chatRoomMembers = chatRoomMembers;
    }

    public static ChatRoom createChatRoom(ChatRoomCreateRequest request) {
        return new ChatRoom(
                request.title(),
                request.type(),
                request.chatRoomMembers()
        );
    }

    public void addMember(Member member) {
        chatRoomMembers.add(new ChatRoomMember(this, member));
    }

    public void removeMember(Member member) {
        chatRoomMembers.removeIf(m -> m.getMember().equals(member));
    }
}