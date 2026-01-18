package hanieum.conik.domain.review.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class ReviewException extends GlobalException {
    public ReviewException(ErrorType errorType) {
        super(errorType);
    }
}