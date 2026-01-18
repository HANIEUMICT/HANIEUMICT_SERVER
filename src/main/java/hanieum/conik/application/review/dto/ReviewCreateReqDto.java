package hanieum.conik.application.review.dto;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.review.entity.Review;
import hanieum.conik.domain.review.enumerate.ReviewTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReviewCreateReqDto(
        @Schema(description = "리뷰 내용", example = "정말 좋은 회사입니다.")
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        String content,

        @Schema(description = "리뷰 태그 목록", example = "[\"REASONABLE_PRICE\", \"HIGH_QUALITY\"]")
        @NotNull(message = "리뷰 태그는 0개에서 3개 선택해야 합니다.")
        List<ReviewTag> tags,

        @Schema(description = "리뷰 이미지 URL 목록", example = "[\"https://cdn.example.com/review/img1.jpg\", \"https://cdn.example.com/review/img2.jpg\"]")
        List<String> imageUrls,

        @Schema(description = "평점", example = "4.5")
        @NotNull(message = "평점은 필수입니다.")
        Double rating
) {
    public Review toEntity(Member member, Long companyId) {
        return Review.create(
                companyId,
                member,
                this.content(),
                this.tags(),
                this.imageUrls(),
                this.rating()
        );
    }
}