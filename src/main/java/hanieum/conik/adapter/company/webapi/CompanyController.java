package hanieum.conik.adapter.company.webapi;

import hanieum.conik.adapter.company.webapi.response.CompanyResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanySaver;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.company.dto.*;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.member.Member;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/company")
@Tag(name = "COMPANY", description = "기업 관련 API")
@Validated
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyFinder companyFinder;
    private final CompanySaver companySaver;
    private final MemberFinder memberFinder;

    @Operation(summary = "기업 등록", description = """
    ## 기업 등록을 수행합니다.
    - 기업 정보를 모두 기입한 후에 기업 등록을 진행합니다.
    - 기업 등록 완료시 기업 id를 반환 받습니다.
    """)
    @PostMapping()
    public ApiResponse<Long> registerCompany(@Valid @RequestBody CompanyRegisterRequest request) {
        return ApiResponse.success(companySaver.register(request));
    }

    @Operation(summary = "기업 정보 수정", description = """
    ## 기업 정보를 수정합니다.
    - 기업 회원이 자신의 기업 정보를 수정합니다.
    - 인증된 회원만 접근할 수 있습니다.
    """
    )
    @PatchMapping()
    public ApiResponse<?> updateCompany(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Valid @RequestBody CompanyUpdateRequest request) {
        Long memberId = Optional.ofNullable(authDetails)
                .map(AuthDetails::getMemberId)
                .orElseThrow(() -> new AuthException(AuthErrorType.UNAUTHORIZED_MEMBER_ACCESS));

        Member member = memberFinder.findById(memberId);
        if (member.getCompanyId() == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND);
        }
        companySaver.update(member.getCompanyId(), request);
        return ApiResponse.success();
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
}
