package hanieum.conik.domain.project.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class ProjectException extends GlobalException {
    public ProjectException(ErrorType errorType) { super(errorType); }
}
