package hanieum.conik.application.review.dto;

import hanieum.conik.domain.review.entity.Review;
import hanieum.conik.domain.review.enumerate.ReviewTag;

import java.util.ArrayList;
import java.util.List;

public record ReviewResDto(
        Long reviewId,
        Long companyId,
        Long memberId,
        String reviewerName,
        String content,
        List<ReviewTag> tags,
        List<String> imageUrls,
        Double rating
) {
    public static ReviewResDto from(Review review) {
        return new ReviewResDto(
                review.getId(),
                review.getCompanyId(),
                review.getMember().getId(),
                review.getMember().getName(),
                review.getContent(),
                new ArrayList<>(review.getTags()),
                new ArrayList<>(review.getImageUrls()),
                review.getRating()
        );
    }
}