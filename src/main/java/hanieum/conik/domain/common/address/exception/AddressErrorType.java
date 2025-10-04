package hanieum.conik.domain.common.address.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AddressErrorType implements ErrorType {
    INVALID_NAME(HttpStatus.BAD_REQUEST, "주소 이름은 필수입니다."),
    INVALID_RECIPIENT(HttpStatus.BAD_REQUEST, "수령인 이름은 필수입니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "전화번호는 필수입니다."),
    INVALID_POSTAL_CODE(HttpStatus.BAD_REQUEST, "우편번호는 필수입니다."),
    INVALID_STREET_ADDRESS(HttpStatus.BAD_REQUEST, "도로명 주소는 필수입니다."),
    INVALID_DETAIL_ADDRESS(HttpStatus.BAD_REQUEST, "상세 주소는 필수입니다.")
    ;

    private final HttpStatus status;

    private final String message;
}
