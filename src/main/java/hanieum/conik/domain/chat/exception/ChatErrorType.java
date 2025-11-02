package hanieum.conik.domain.chat.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorType implements ErrorType {
    INVALID_MESSAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 메시지입니다.");


    private final HttpStatus status;
    private final String message;
}
