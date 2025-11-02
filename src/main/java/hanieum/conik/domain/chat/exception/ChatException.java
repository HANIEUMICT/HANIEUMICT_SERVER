package hanieum.conik.domain.chat.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class ChatException extends GlobalException {
    public ChatException(ErrorType errorType) {
        super(errorType);
    }
}
