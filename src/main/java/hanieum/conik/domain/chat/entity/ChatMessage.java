package hanieum.conik.domain.chat.entity;

import hanieum.conik.domain.chat.dto.ChatMessageDto;
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
    private Long roomId;

    private Long senderId;

    private String content;

    private MessageType type;

    private String fileUrl;   // IMAGE/FILE/VIDEO 시 파일 위치 (S3, CloudFront 등)

    private String fileName;  // 파일 이름

    private Long fileSize;    // 파일 크기

    private Long seq;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    /** 메시지 생성 시 검증 */
    public static ChatMessage create(ChatMessageDto req, long seq) {
        MessageType mt = (req.type() != null) ? req.type() : MessageType.TEXT;

        switch (mt) {
            case TEXT -> {
                if (req.content() == null || req.content().isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 내용 필요
                }
            }
            case IMAGE, VIDEO, FILE -> {
                if (req.fileUrl() == null || req.fileUrl().isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 파일 URL 필요
                }
            }
            case SYSTEM -> {
                if (req.content() == null || req.content().isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 시스템 메시지 내용 필요
                }
            }
        }

        return ChatMessage.builder()
                .roomId(req.roomId())
                .senderId(req.senderId())
                .type(mt)           // ← enum 저장 (ordinal 쓰지 않기)
                .content(req.content())    // TEXT/SYSTEM이면 사용
                .fileUrl(req.fileUrl())    // IMAGE/VIDEO/FILE이면 사용
                .fileName(req.fileName())
                .fileSize(req.fileSize())
                .seq(seq)
                .createdAt(Instant.now())
                .build();
    }

    /** 시스템 메시지 생성 (예: 누가 입장했을 때) */
    public static ChatMessage system(Long roomId, String content, long seq) {
        ChatMessageDto systemMessage = ChatMessageDto.system(roomId, content);
        return ChatMessage.create(systemMessage, seq);
    }
}
