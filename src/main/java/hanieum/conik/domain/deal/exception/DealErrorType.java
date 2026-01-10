package hanieum.conik.domain.deal.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DealErrorType implements ErrorType {

    INVALID_DEAL_STEP(HttpStatus.BAD_REQUEST, "유효하지 않은 거래 단계입니다."),
    DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 거래를 찾을 수 없습니다.");

    private final HttpStatus status;

    private final String message;

}
