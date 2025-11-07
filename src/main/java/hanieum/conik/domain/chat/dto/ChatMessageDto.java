package hanieum.conik.domain.chat.dto;

import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.enumerate.MessageType;

import java.time.Instant;

public record ChatMessageDto(
        Long roomId,
        Long senderId,
        MessageType type,
        String content,             // TEXT인 경우에만 사용
        String fileUrl,             // IMAGE, VIDEO, FILE 타입 시 S3 등 파일 경로
        String fileName,            // 파일 이름
        Long fileSize,              // 파일 크기
        Instant createdAt
) {
    public static ChatMessageDto text(ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getRoomId(),
                chatMessage.getSenderId(),
                MessageType.TEXT,
                chatMessage.getContent(),
                null,
                null,
                null,
                chatMessage.getCreatedAt());
    }

    public static ChatMessageDto system(Long roomId, String content) {
        return new ChatMessageDto(roomId, null, MessageType.SYSTEM, content, null, null, null, Instant.now());
    }

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getType(),
                chatMessage.getContent(),
                chatMessage.getFileUrl(),
                chatMessage.getFileName(),
                chatMessage.getFileSize(),
                chatMessage.getCreatedAt()
        );
    }
}