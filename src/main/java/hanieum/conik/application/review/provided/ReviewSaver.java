package hanieum.conik.application.review.provided;

import hanieum.conik.application.review.dto.ReviewCreateReqDto;
import hanieum.conik.application.review.dto.ReviewUpdateReqDto;

public interface ReviewSaver {
    Long saveReview(ReviewCreateReqDto requestDto, Long memberId, Long companyId);
    void deleteReview(Long reviewId, Long memberId);
    void updateReview(Long reviewId, Long memberId, ReviewUpdateReqDto requestDto);
}