package hanieum.conik.adapter.deal.webapi;

import hanieum.conik.adapter.deal.dto.response.DealSummaryResponse;
import hanieum.conik.adapter.deal.dto.response.DealTimelineResponse;
import hanieum.conik.application.deal.provided.DealFinder;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/deal")
@Tag(name = "DEAL", description = "거래(주문정보) 관련 API")
@Validated
@RequiredArgsConstructor
public class DealController {

    private final DealFinder dealFinder;

    @Operation(summary = "내 거래 수락된 프로젝트들의 주문 정보 조회", description = """
    ## 내 프로젝트들의 주문 정보 리스트를 조회합니다.
    - 거래 수락 이후 상태인 프로젝트들에 대해서만 조회됩니다.
    - 기본 5개씩 페이지네이션 됩니다.
    """)
    @GetMapping
    public ApiResponse<Page<DealSummaryResponse>> getDeals(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ApiResponse.success(dealFinder.getDeals(authDetails, pageable));
    }

    @Operation( summary = "거래 상세 진행 타임라인 조회", description = """
    ## 거래(Deal)의 전체 진행 단계를 타임라인 형태로 조회합니다.
    - DealStep 전체를 순서대로 반환합니다.
    - 각 단계별 상세 정보가 없을 경우 detail은 null로 반환됩니다.
    """
    )
    @GetMapping("/{dealId}")
    public ApiResponse<DealTimelineResponse> getDealTimeline(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long dealId
    ) {
        return ApiResponse.success(
                dealFinder.getDealTimeline(dealId)
        );
    }
}
