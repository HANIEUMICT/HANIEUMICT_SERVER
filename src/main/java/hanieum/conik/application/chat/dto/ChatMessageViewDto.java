package hanieum.conik.application.chat.dto;

import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.enumerate.MessageType;

import java.time.Instant;

public record ChatMessageViewDto(
        long seq,
        Long roomId,
        Long senderId,
        MessageType type,
        String content,
        String fileUrl,
        String fileName,
        Long fileSize,
        Instant createdAt,
        long unreadCount
) {
    public static ChatMessageViewDto from(ChatMessageDto dto, long unreadCount) {
        return new ChatMessageViewDto(
                dto.seq(),
                dto.roomId(),
                dto.senderId(),
                dto.type(),
                dto.content(),
                dto.fileUrl(),
                dto.fileName(),
                dto.fileSize(),
                dto.createdAt(),
                unreadCount
        );
    }
}
