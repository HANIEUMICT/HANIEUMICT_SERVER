package hanieum.conik.domain.deal.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DealErrorType implements ErrorType {

    INVALID_DEAL_STEP(HttpStatus.BAD_REQUEST, "유효하지 않은 거래 단계입니다."),
    DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 거래를 찾을 수 없습니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, "거래 데이터가 손상되었습니다."),
    COMPANY_NOT_ASSIGNED(HttpStatus.FORBIDDEN, "기업에 소속되지 않은 사용자입니다."),
    INVALID_DEAL_ACCESS(HttpStatus.FORBIDDEN, "거래 조회 권한이 없습니다.");

    private final HttpStatus status;

    private final String message;

}
