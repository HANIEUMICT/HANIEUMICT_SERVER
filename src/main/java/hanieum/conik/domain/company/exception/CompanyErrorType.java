package hanieum.conik.domain.company.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorType implements ErrorType {
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 기업입니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다.")
    ;

    public final HttpStatus status;

    public final String message;
}
