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
    PROPOSAL_INITIATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "제안서 생성 중 오류가 발생했습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
