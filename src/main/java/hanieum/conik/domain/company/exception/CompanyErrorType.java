package hanieum.conik.domain.company.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorType implements ErrorType {
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 기업입니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "페이지/사이즈 값이 올바르지 않습니다."),
    COMPANY_DETAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 기업 상세가 등록되어 있습니다."),
    COMPANY_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "기업 상세가 존재하지 않습니다."),
    MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "엔티티/DTO 매핑 중 오류가 발생했습니다."),
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장비입니다."),
    PORTFOLIO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 포트폴리오입니다.")
    ;

    public final HttpStatus status;

    public final String message;
}
