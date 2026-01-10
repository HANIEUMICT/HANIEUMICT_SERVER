package hanieum.conik.domain.deal.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class DealException extends GlobalException {
    public DealException(ErrorType errorType) { super(errorType); }
}
