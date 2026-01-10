package hanieum.conik.domain.chat.entity;

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
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "room_seq_idx", def = "{'roomId': 1, 'seq': -1}")
public class ChatMessage{
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private Long senderId;

    private String content;

    private MessageType type;

    private String fileUrl;   // IMAGE/FILE/VIDEO 시

    private String fileName;  // 파일 이름

    private Long fileSize;    // 파일 크기

    private long seq;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    /** 메시지 생성 */
    public static ChatMessage create(
            Long roomId,
            Long senderId,
            String content,
            MessageType type,
            String fileUrl,
            String fileName,
            Long fileSize,
            long seq
    ){
        if(roomId == null || senderId == null){
            throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 필수 값 누락
        }

        MessageType mt = (type != null) ? type : MessageType.TEXT;

        switch (mt) {
            case TEXT -> {
                if (content == null || content.isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 내용 필요
                }
            }
            case IMAGE, VIDEO, FILE -> {
                if (fileUrl == null || fileUrl.isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 파일 URL 필요
                }
            }
            case SYSTEM -> {
                if (content == null || content.isBlank()) {
                    throw new ChatException(ChatErrorType.INVALID_MESSAGE); // 시스템 메시지 내용 필요
                }
            }
        }

        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .type(mt)
                .content(content)    // TEXT/SYSTEM이면 사용
                .fileUrl(fileUrl)    // IMAGE/VIDEO/FILE이면 사용
                .fileName(fileName)
                .fileSize(fileSize)
                .seq(seq)
                .build();
    }
}
