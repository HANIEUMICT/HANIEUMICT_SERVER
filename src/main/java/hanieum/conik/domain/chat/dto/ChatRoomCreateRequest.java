package hanieum.conik.domain.chat.dto;

import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChatRoomCreateRequest(
        @Schema(description = "채팅방 타입", example = "GROUP")
        ChatRoomType type,

        @Schema(description = "참여자 ID 목록", example = "[1, 2, 3]")
        List<Long> memberIds
) {}
