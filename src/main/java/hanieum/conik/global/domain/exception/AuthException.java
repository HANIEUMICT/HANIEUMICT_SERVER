package hanieum.conik.global.domain.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class AuthException extends GlobalException {
    public AuthException(ErrorType errorType) {
        super(errorType);
    }
}
