package hanieum.conik.application.review;

import hanieum.conik.application.review.dto.ReviewResDto;
import hanieum.conik.application.review.provided.ReviewFinder;
import hanieum.conik.application.review.required.ReviewRepository;
import hanieum.conik.domain.review.entity.Review;
import hanieum.conik.domain.review.exception.ReviewErrorType;
import hanieum.conik.domain.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewFinderService implements ReviewFinder {
    private final ReviewRepository reviewRepository;

    @Override
    public Page<ReviewResDto> findCompanyReviews(Long companyId, Pageable pageable) {
        Page<Long> reviewIds = reviewRepository.findReviewIdsByCompanyId(companyId, pageable);
        List<Review> reviews = reviewRepository.findByIdsWithMember(reviewIds.getContent());

        return new PageImpl<>(
                reviews.stream().map(ReviewResDto::from).toList(),
                pageable,
                reviewIds.getTotalElements()
        );
    }

    @Override
    public Page<ReviewResDto> findMemberReviews(Long memberId, Pageable pageable) {
        Page<Long> reviewIds = reviewRepository.findReviewIdsByMemberId(memberId, pageable);
        List<Review> reviews = reviewRepository.findByIdsWithMember(reviewIds.getContent());

        return new PageImpl<>(
                reviews.stream().map(ReviewResDto::from).toList(),
                pageable,
                reviewIds.getTotalElements()
        );
    }

    @Override
    public ReviewResDto findReview(Long reviewId) {
        Review review = reviewRepository.findByIdWithMember(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorType.REVIEW_NOT_FOUND));
        return ReviewResDto.from(review);
    }
}