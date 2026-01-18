package hanieum.conik.application.review;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.review.dto.ReviewCreateReqDto;
import hanieum.conik.application.review.dto.ReviewUpdateReqDto;
import hanieum.conik.application.review.provided.ReviewSaver;
import hanieum.conik.application.review.required.ReviewRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.review.entity.Review;
import hanieum.conik.domain.review.exception.ReviewErrorType;
import hanieum.conik.domain.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewModifyService implements ReviewSaver {
    private final ReviewRepository reviewRepository;
    private final CompanyFinder companyFinder;
    private final MemberFinder memberFinder;

    @Override
    public Long saveReview(ReviewCreateReqDto requestDto, Long memberId, Long companyId) {
        // 회사 검증
        companyFinder.findCompany(companyId);
        // 회원 검증
        Member member = memberFinder.findById(memberId);

        Review review = requestDto.toEntity(member, companyId);
        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    @Override
    public void deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorType.REVIEW_NOT_FOUND));
        if (!review.getMember().getId().equals(memberId)) {
            throw new ReviewException(ReviewErrorType.MEMBER_ID_MISMATCH);
        }
        reviewRepository.delete(review);
    }

    @Override
    public void updateReview(Long reviewId, Long memberId, ReviewUpdateReqDto requestDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorType.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(memberId)) {
            throw new ReviewException(ReviewErrorType.MEMBER_ID_MISMATCH);
        }
        review.update(
                requestDto.content(),
                requestDto.tags(),
                requestDto.imageUrls(),
                requestDto.rating()
        );
    }
}