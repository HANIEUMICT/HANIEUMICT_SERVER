package hanieum.conik.domain.chat.dto;

import hanieum.conik.domain.chat.entity.ChatRoomMember;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChatRoomCreateRequest(
        @Schema(description = "채팅방 이름", example = "마이데이 단톡방")
        String title,
        ChatRoomType type,
        List<ChatRoomMember> chatRoomMembers
) {}
