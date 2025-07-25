package hanieum.conik.domain.proposal.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProposalErrorType implements ErrorType {
    PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제안서를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
