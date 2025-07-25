package hanieum.conik.domain.proposal.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;

public class ProposalException extends GlobalException {
    public ProposalException(ErrorType errorType) { super(errorType); }
}
