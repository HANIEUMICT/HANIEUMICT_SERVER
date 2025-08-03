package hanieum.conik.domain.company.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class CompanyException extends GlobalException {
    public CompanyException(ErrorType errorType) {
        super(errorType);
    }
}
