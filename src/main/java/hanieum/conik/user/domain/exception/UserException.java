package hanieum.conik.user.domain.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class UserException extends GlobalException {

    public UserException(ErrorType errorType) {
        super(errorType);
    }
}
