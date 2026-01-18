package hanieum.conik.application.review.provided;

import hanieum.conik.application.review.dto.ReviewResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewFinder {
    Page<ReviewResDto> findCompanyReviews(Long companyId, Pageable pageable);
    Page<ReviewResDto> findMemberReviews(Long memberId, Pageable pageable);
    ReviewResDto findReview(Long reviewId);
}