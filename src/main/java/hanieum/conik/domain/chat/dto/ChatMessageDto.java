package hanieum.conik.domain.chat.dto;

import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.enumerate.MessageType;
import org.bson.Document;

import java.time.Instant;

public record ChatMessageDto(
        long seq,
        Long roomId,
        Long senderId,
        MessageType type,
        String senderName,
        String content,             // TEXT인 경우에만 사용
        String fileUrl,             // IMAGE, VIDEO, FILE 타입 시 S3 등 파일 경로
        String fileName,            // 파일 이름
        Long fileSize,              // 파일 크기
        Instant createdAt
) {
    public static ChatMessageDto fromEntity(ChatMessage chatMessage, String senderName) {
        return new ChatMessageDto(
                chatMessage.getSeq(),
                chatMessage.getRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getType(),
                senderName,
                chatMessage.getContent(),
                chatMessage.getFileUrl(),
                chatMessage.getFileName(),
                chatMessage.getFileSize(),
                chatMessage.getCreatedAt()
        );
    }

    public static ChatMessageDto fromDocument(Document doc) {
        if (doc == null) {
            return null;
        }

        return new ChatMessageDto(
                doc.getLong("seq"),
                doc.getLong("roomId"),
                doc.getLong("senderId"),
                MessageType.valueOf(doc.getString("type")),
                null,
                doc.getString("content"),
                doc.getString("fileUrl"),
                doc.getString("fileName"),
                doc.getLong("fileSize"),
                doc.getDate("createdAt").toInstant()
        );
    }
}