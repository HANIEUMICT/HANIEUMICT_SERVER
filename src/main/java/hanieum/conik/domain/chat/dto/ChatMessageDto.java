package hanieum.conik.domain.chat.dto;

import hanieum.conik.adapter.chat.dto.ChatMessageRequest;
import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.enumerate.MessageType;
import org.bson.Document;

import java.time.Instant;
import java.util.Date;

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

    public static ChatMessageDto fromRequest(Long roomId, ChatMessageRequest request) {
        return new ChatMessageDto(
                0L, // seq는 저장 후에 할당되므로 임시로 0L 사용
                roomId,
                request.senderId(),
                request.type(),
                null, // senderName은 나중에 채워질 수 있음
                request.content(),
                request.fileUrl(),
                request.fileName(),
                request.fileSize(),
                null  // createdAt은 저장 후에 할당되므로 임시로 null 사용
        );
    }

    public static ChatMessageDto fromDocument(Document doc) {
        if (doc == null) {
            return null;
        }

        String typeStr = doc.getString("type");
        MessageType messageType;
        try {
            if (typeStr != null) {
                messageType = MessageType.valueOf(typeStr);
            } else {
                messageType = MessageType.TEXT;
            }
        } catch (IllegalArgumentException e) {
            messageType = MessageType.TEXT;
        }

        Date date = doc.getDate("createdAt");
        Instant createdAt = (date != null) ? date.toInstant() : null;

        return new ChatMessageDto(
                doc.getLong("seq"),
                doc.getLong("roomId"),
                doc.getLong("senderId"),
                messageType, // 수정된 변수 사용
                null,
                doc.getString("content"),
                doc.getString("fileUrl"),
                doc.getString("fileName"),
                doc.getLong("fileSize"),
                createdAt    // 수정된 변수 사용
        );
    }
}