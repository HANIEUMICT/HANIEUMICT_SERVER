package hanieum.conik.application.review;

import hanieum.conik.application.review.dto.ReviewResDto;
import hanieum.conik.application.review.provided.ReviewFinder;
import hanieum.conik.application.review.required.ReviewRepository;
import hanieum.conik.domain.review.entity.Review;
import hanieum.conik.domain.review.exception.ReviewErrorType;
import hanieum.conik.domain.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewFinderService implements ReviewFinder {
    private final ReviewRepository reviewRepository;

    @Override
    public Page<ReviewResDto> findCompanyReviews(Long companyId, Pageable pageable) {
        return reviewRepository.findByCompanyIdWithMember(companyId, pageable)
                .map(ReviewResDto::from);
    }

    @Override
    public Page<ReviewResDto> findMemberReviews(Long memberId, Pageable pageable) {
        return reviewRepository.findByMemberId(memberId, pageable)
                .map(ReviewResDto::from);
    }

    @Override
    public ReviewResDto findReview(Long reviewId) {
        Review review = reviewRepository.findByIdWithMember(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorType.REVIEW_NOT_FOUND));
        return ReviewResDto.from(review);
    }
}