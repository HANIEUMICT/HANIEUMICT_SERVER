package hanieum.conik.adapter.review;

import hanieum.conik.application.review.dto.ReviewResDto;
import hanieum.conik.application.review.dto.ReviewUpdateReqDto;
import hanieum.conik.application.review.provided.ReviewFinder;
import hanieum.conik.application.review.provided.ReviewSaver;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "REVIEW")
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewSaver reviewSaver;
    private final ReviewFinder reviewFinder;

    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정합니다.")
    @PatchMapping("/{reviewId}")
    public ApiResponse<?> updateReview(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateReqDto reviewUpdateReqDto
    ) {
        reviewSaver.updateReview(reviewId, authDetails.getMemberId(), reviewUpdateReqDto);
        return ApiResponse.success("리뷰 수정 성공");
    }

    @Operation(
            summary = "리뷰 삭제 API",
            description = "리뷰를 삭제합니다."
    )
    @DeleteMapping("/{reviewId}")
    public ApiResponse<?> deleteReview(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long reviewId
    ) {
        reviewSaver.deleteReview(reviewId, authDetails.getMemberId());
        return ApiResponse.success("리뷰 삭제 성공");
    }

    @Operation(
            summary = "회원 리뷰 조회 API",
            description = "로그인한 회원이 작성한 리뷰들을 조회합니다."
    )
    @GetMapping("/me")
    public ApiResponse<Page<ReviewResDto>> getMemberReviews(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(reviewFinder.findMemberReviews(authDetails.getMemberId(), pageable));
    }

    @Operation(
            summary = "리뷰 단건 조회 API",
            description = "리뷰를 단건 조회합니다."
    )
    @GetMapping("/{reviewId}")
    public  ApiResponse<ReviewResDto> getReview(
            @PathVariable Long reviewId
    ) {return ApiResponse.success(reviewFinder.findReview(reviewId));}
}