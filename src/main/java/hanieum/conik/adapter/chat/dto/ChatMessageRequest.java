package hanieum.conik.adapter.chat.dto;

import hanieum.conik.domain.chat.enumerate.MessageType;

public record ChatMessageRequest(
        Long roomId,
        Long senderId,
        String content,
        MessageType type,
        String fileUrl,
        String fileName,
        Long fileSize
) {}