package hanieum.conik.domain.chat.dto;

import hanieum.conik.domain.chat.enumerate.MessageType;

public record ChatMessageRequest(
        String roomId,
        String senderId,
        String content,
        MessageType type
) {}