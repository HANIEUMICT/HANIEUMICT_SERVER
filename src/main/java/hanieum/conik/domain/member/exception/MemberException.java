package hanieum.conik.domain.member.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class MemberException extends GlobalException {

    public MemberException(ErrorType errorType) {
        super(errorType);
    }
}
