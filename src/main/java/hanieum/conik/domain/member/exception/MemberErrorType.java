package hanieum.conik.domain.member.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorType implements ErrorType {
    INVALID_AUTHORIZATION_CODE(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    TERMS_NOT_AGREED(HttpStatus.BAD_REQUEST, "약관동의를 해야 가입이 가능합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_ROLE_FOR_COMPANY(HttpStatus.UNAUTHORIZED, "기업 회원만 회사에 등록될 수 있습니다"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,     "존재하지 않는 사용자입니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    COMPANY_ID_MISSING(HttpStatus.INTERNAL_SERVER_ERROR, "멤버의 회사 정보가 누락되었습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED,   "유효하지 않은 이메일 인증 코드입니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED,   "이메일 인증 코드가 만료되었습니다.")
    ;

    private final HttpStatus status;

    private final String message;
}
