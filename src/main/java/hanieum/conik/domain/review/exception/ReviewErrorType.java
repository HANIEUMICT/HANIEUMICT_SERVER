package hanieum.conik.domain.review.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorType implements ErrorType {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    COMPANY_IS_NULL(HttpStatus.BAD_REQUEST, "회사가 null일 수 없습니다."),
    MEMBER_IS_NULL(HttpStatus.BAD_REQUEST, "멤버가 null일 수 없습니다."),
    CONTENT_IS_INVALID(HttpStatus.BAD_REQUEST, "리뷰 내용이 유효하지 않습니다."),
    RATING_IS_INVALID(HttpStatus.BAD_REQUEST, "리뷰 평점은 0점에서 5점 사이여야 합니다."),
    TOO_MANY_TAGS(HttpStatus.BAD_REQUEST, "리뷰 태그는 최대 3개까지 선택할 수 있습니다."),
    MEMBER_ID_MISMATCH(HttpStatus.FORBIDDEN, "리뷰 작성자만 리뷰를 수정할 수 있습니다.");
    ;

    public final HttpStatus status;
    public final String message;
}