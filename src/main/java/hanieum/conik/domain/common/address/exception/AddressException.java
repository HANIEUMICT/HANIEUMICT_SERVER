package hanieum.conik.domain.common.address.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class AddressException extends GlobalException {
    public AddressException(ErrorType errorType) {
        super(errorType);
    }
}
