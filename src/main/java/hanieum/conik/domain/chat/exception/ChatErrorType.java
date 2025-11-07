package hanieum.conik.domain.chat.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorType implements ErrorType {
    INVALID_MESSAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 메시지입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    MEMBER_NOT_IN_CHAT_ROOM(HttpStatus.FORBIDDEN, "채팅방에 속하지 않은 사용자입니다."),
    ALREADY_IN_CHAT_ROOM(HttpStatus.CONFLICT, "이미 채팅방에 속한 사용자입니다."),
    NOT_INCLUDED_IN_CHAT_ROOM(HttpStatus.BAD_REQUEST, "채팅방에 포함되지 않은 사용자입니다."),
    PRIVATE_CHAT_ROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "1:1 채팅방이 이미 존재합니다.");

    private final HttpStatus status;
    private final String message;
}
