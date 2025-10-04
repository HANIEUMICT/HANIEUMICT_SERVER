package hanieum.conik.adapter.company.webapi;

import hanieum.conik.adapter.company.webapi.response.CompanyDetailResponse;
import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.adapter.company.webapi.response.CompanyResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanySaver;
import hanieum.conik.domain.company.dto.CompanyDetailCreateRequest;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/company")
@Tag(name = "COMPANY", description = "기업 관련 API")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyFinder companyFinder;
    private final CompanySaver companySaver;

    @Operation(summary = "기업 등록", description = """
    ## 기업 등록을 수행합니다.
    - 기업 정보를 모두 기입한 후에 기업 등록을 진행합니다.
    - 기업 등록 완료시 기업 id를 반환 받습니다.
    """)
    @PostMapping()
    public ApiResponse<Long> registerCompany(@Valid @RequestBody CompanyRegisterRequest request) {
        return ApiResponse.success(companySaver.register(request));
    }

    @Operation(summary = "기업 summary 조회(이름, 지역, 업종명)", description = """
    ## 회원가입시 기업의 요약 정보를 조회합니다.
    - 기업의 이름, 지역, 업종명 등의 요약 정보를 조회합니다.
    - 페이징 처리가 적용되어 있습니다.
    - 정렬은 생성일자 내림차순으로 고정되어 있습니다.
    """)
    @GetMapping("/summaries")
    public ApiResponse<Page<CompanySummaryResponse>> findCompaniesSummary(
            @ParameterObject CompanySummarySearchCondition cond,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(companyFinder.searchCompanySummaries(cond, pageable));
    }

    @Operation(summary = "특정 기업 조회", description = """
    ## 특정 기업 조회를 수행합니다.
    - 기업id를 통해 특정 기업을 조회할 수 있습니다.
    """)
    @GetMapping("/{companyId}")
    public ApiResponse<CompanyResponse> findCompany(@PathVariable Long companyId) {
        Company company = companyFinder.findCompany(companyId);
        return ApiResponse.success(CompanyResponse.from(company));
    }

    @Operation(summary = "기업 상세 정보 등록", description = """
                ## 기업 회원이 기업의 상세 정보를 등록합니다.
                - 기업 상세 정보 + 장비/포트폴리오 목록을 한 번에 전송합니다.
                """)
    @PostMapping("/detail")
    public ApiResponse<Long> registerCompanyDetail(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Valid @RequestBody CompanyDetailCreateRequest request
    ) {
        Long memberId = Optional.ofNullable(authDetails)
                .map(AuthDetails::getMemberId)
                .orElseThrow(() -> new AuthException(AuthErrorType.UNAUTHORIZED_MEMBER_ACCESS));

        return ApiResponse.success(companySaver.registerCompanyDetail(memberId, request)
        );
    }

    @Operation(summary = "기업 상세 페이지 단건 조회", description = """
            ## 기업 상세 페이지를 조회합니다.
            - 기업 상세 내용이 입력되지 않은 경우 예외가 터집니다.(조회 불가능)
            """)
    @GetMapping("/detail/{companyId}")
    public ApiResponse<CompanyDetailResponse> findCompanyWithDetail(@PathVariable Long companyId){
        return ApiResponse.success(companyFinder.findCompanyWithDetail(companyId));
    }

    @Operation(summary = "기업 상세 마이페이지 조회", description = """
            ## 기업 회원 상세페이지 조회합니다.
            - 기업 회원이 자신의 기업 상세 페이지를 조회합니다.
            - 기업 상세 내용이 등록되지 않으면 상세 내용은 null로 반환됩니다.
            """)
    @GetMapping("/detail/me")
    public ApiResponse<CompanyDetailResponse> findMyCompanyWithDetail(@AuthenticationPrincipal AuthDetails authDetails){
        Long memberId = Optional.ofNullable(authDetails)
                .map(AuthDetails::getMemberId)
                .orElseThrow(() -> new AuthException(AuthErrorType.UNAUTHORIZED_MEMBER_ACCESS));

        return ApiResponse.success(companyFinder.findMyCompanyWithDetail(memberId));
    }

    @Operation(summary = "기업 프로필 목록 조회(필터 적용)", description = """
            ## 기업의 프로필 목록을 조회합니다.
            - 필터를 적용해 원하는 기업의 프로필 목록을 조회합니다.
            - 기업 상세 정보를 입력하지 않으면 프로필 목록 조회에서 제외됩니다.
            - 필터가 존재하지 않으면 거래 건수가 많은 순서대로 조회됩니다.
            """)
    @GetMapping("/profiles")
    public ApiResponse<Page<CompanyProfileResponse>> searchProfiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxResponseMinutes,
            @RequestParam(required = false) Integer minTotalOrderCount,
            @RequestParam(required = false) Integer maxProductionHours,
            @ParameterObject @PageableDefault(size = 20, sort = "rating", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        var cond = new CompanyProfileSearchCondition(keyword, minRating, maxResponseMinutes, minTotalOrderCount, maxProductionHours);
        return ApiResponse.success(companyFinder.findAllCompanyWithFilter(cond, pageable));
    }
}
