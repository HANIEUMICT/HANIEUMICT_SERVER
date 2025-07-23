package hanieum.conik.domain.member.exception;

import hanieum.conik.global.apiPayload.exception.GlobalException;

public class VerifyEmailException extends GlobalException {

    // 1) private 생성자로 오직 UserErrorType만 받음
    private VerifyEmailException(MemberErrorType type) {
        super(type);
    }

    // 2) 인증 코드가 틀렸을 때
    public static VerifyEmailException invalid() {
        return new VerifyEmailException(MemberErrorType.INVALID_VERIFICATION_CODE);
    }

    // 3) 인증 코드가 만료됐을 때
    public static VerifyEmailException expired() {
        return new VerifyEmailException(MemberErrorType.EXPIRED_VERIFICATION_CODE);
    }
}
