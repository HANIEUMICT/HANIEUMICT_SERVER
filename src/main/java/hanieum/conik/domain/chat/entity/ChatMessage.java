package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.chat.dto.ChatMessageRequest;
import hanieum.conik.domain.chat.enumerate.MessageType;
import hanieum.conik.domain.chat.exception.ChatErrorType;
import hanieum.conik.domain.chat.exception.ChatException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage{
    @Id
    private String id;

    @Indexed
    private String roomId;

    private String senderId;

    private String content;

    private MessageType type;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    /** 메시지 생성 시 검증 */
    public static ChatMessage create(ChatMessageRequest request) {
        if (request.content() == null || request.content().isBlank()) {
            throw new ChatException(ChatErrorType.INVALID_MESSAGE);
        }
        return ChatMessage.builder()
                .roomId(request.roomId())
                .senderId(request.senderId())
                .content(request.content())
                .type(request.type())
                .createdAt(Instant.now())
                .build();
    }

    /** 시스템 메시지 생성 (예: 누가 입장했을 때) */
    public static ChatMessage system(String roomId, String content) {
        ChatMessageRequest systemMessage = new ChatMessageRequest(roomId, "SYSTEM", content, MessageType.SYSTEM);
        return ChatMessage.create(systemMessage);
    }
}
