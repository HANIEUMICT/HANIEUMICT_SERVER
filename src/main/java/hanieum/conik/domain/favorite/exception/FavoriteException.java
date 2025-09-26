package hanieum.conik.domain.favorite.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class FavoriteException extends GlobalException {
    public FavoriteException(ErrorType errorType) {
        super(errorType);
    }
}
