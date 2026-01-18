package hanieum.conik.application.review.dto;

import hanieum.conik.domain.review.enumerate.ReviewTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReviewUpdateReqDto(
        @Schema(description = "리뷰 내용", example = "정말 너~~~~~~무 좋은 회사입니다.")
        String content,

        @Schema(description = "리뷰 태그 목록", example = "[\"REASONABLE_PRICE\", \"HIGH_QUALITY\"]")
        List<ReviewTag> tags,

        @Schema(description = "리뷰 이미지 URL 목록", example = "[\"https://cdn.example.com/review/img1.jpg\", \"https://cdn.example.com/review/img2.jpg\"]")
        List<String> imageUrls,

        @Schema(description = "평점", example = "4.5")
        Double rating
) {}