package hanieum.conik.domain.member.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class UserException extends GlobalException {

    public UserException(ErrorType errorType) {
        super(errorType);
    }
}
