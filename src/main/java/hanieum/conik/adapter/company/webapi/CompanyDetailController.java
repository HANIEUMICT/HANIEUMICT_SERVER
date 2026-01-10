package hanieum.conik.adapter.company.webapi;

import hanieum.conik.adapter.company.response.CompanyDetailResponse;
import hanieum.conik.adapter.company.response.CompanyProfileResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanySaver;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.company.dto.*;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.member.Member;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/v1/company/detail")
@Tag(name = "COMPANY_DETAIL", description = "기업 상세 관련 API")
@Validated
@RequiredArgsConstructor
public class CompanyDetailController {
    private final CompanyFinder companyFinder;
    private final CompanySaver companySaver;
    private final MemberFinder memberFinder;

    @Operation(summary = "기업 상세 정보 등록", description = """
                ## 기업 회원이 기업의 상세 정보를 등록합니다.
                - 기업 상세 정보 + 장비/포트폴리오 목록을 한 번에 전송합니다.
                """)
    @PostMapping()
    public ApiResponse<Long> registerCompanyDetail(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Valid @RequestBody CompanyDetailCreateRequest request
    ) {
        Long companyId = memberFinder.findCompanyIdByMemberId(authDetails.getMemberId());

        return ApiResponse.success(companySaver.registerCompanyDetail(companyId, request));
    }

    @Operation(summary = "기업 상세 정보 수정", description = """
            ## 기업 회원이 자신의 기업 상세 정보를 수정합니다.
            - 기업 상세 정보 + 장비/포트폴리오 목록을 한 번에 전송합니다.
            - Optimistic Lock을 위해 If-Match 헤더에 수정 시점의 epoch milli 값을 포함해야 합니다.
            - If-Match 헤더의 값은 기업 상세 정보를 조회할 때 응답 헤더의 ETag 값과 동일합니다.
            """)
    @PutMapping("/me")
    public ApiResponse<?> updateCompanyDetail(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestHeader(value = "If-Match", required = false) String ifMatch,
            @Valid @RequestBody CompanyDetailUpdateRequest request
    ) {
        Member member = memberFinder.findById(authDetails.getMemberId());
        Long companyId = member.getCompanyId();

        if(companyId == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DOES_NOT_BELONG_TO_MEMBER);
        }

        long ifMatchEpochMilli = parseIfMatchOrThrow(ifMatch);

        companySaver.updateCompanyDetail(companyId, ifMatchEpochMilli, request);

        return ApiResponse.success("기업 상세 정보가 수정되었습니다.");
    }

    @Operation(summary = "기업 상세 페이지 단건 조회", description = """
            ## 기업 상세 페이지를 조회합니다.
            - 기업 상세 내용이 입력되지 않은 경우 예외가 터집니다.(조회 불가능)
            """)
    @GetMapping("/{companyId}")
    public ApiResponse<CompanyDetailResponse> findCompanyWithDetail(@PathVariable Long companyId){
        return ApiResponse.success(companyFinder.findCompanyWithDetail(companyId));
    }

    @Operation(summary = "기업 상세 마이페이지 조회", description = """
            ## 기업 회원 상세페이지 조회합니다.
            - 기업 회원이 자신의 기업 상세 페이지를 조회합니다.
            - 기업 상세 내용이 등록되지 않으면 상세 내용은 null로 반환됩니다.
            """)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CompanyDetailResponse>> findMyCompanyWithDetail(
            @AuthenticationPrincipal AuthDetails authDetails)
    {
        Member member = memberFinder.findById(authDetails.getMemberId());
        Company company = companyFinder.findCompany(member.getCompanyId());

        CompanyDetailResponse response = companyFinder.findCompanyWithDetail(company.getId());

        if (response.detail() == null) {
            return ResponseEntity.ok().body(ApiResponse.success(response));
        }

        long epochMilli = response.detail().modifiedAt().toInstant(ZoneOffset.UTC).toEpochMilli();

        return ResponseEntity.ok().eTag("\"" + epochMilli + "\"").body(ApiResponse.success(response));

    }

    @Operation(summary = "기업 프로필 목록 조회(필터 적용)", description = """
            ## 기업의 프로필 목록을 조회합니다.
            - 필터를 적용해 원하는 기업의 프로필 목록을 조회합니다.
            - 기업 상세 정보를 입력하지 않으면 프로필 목록 조회에서 제외됩니다.
            """)
    @GetMapping("/profiles")
    public ApiResponse<Page<CompanyProfileResponse>> searchProfiles(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxResponseMinutes,
            @RequestParam(required = false) Integer minTotalOrderCount,
            @RequestParam(required = false) Integer maxProductionHours,
            @ParameterObject @PageableDefault(size = 20, sort = "rating", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        var cond = new CompanyProfileSearchCondition(categories, minRating, maxResponseMinutes, minTotalOrderCount, maxProductionHours);
        return ApiResponse.success(companyFinder.findAllCompanyWithFilter(cond, pageable));
    }

    private long parseIfMatchOrThrow(String ifMatch) {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new GlobalException(GlobalErrorType.PRECONDITION_REQUIRED); // 428
        }
        String s = ifMatch.trim();
        if (s.startsWith("W/")) s = s.substring(2).trim();
        if (s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length() - 1);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new GlobalException(GlobalErrorType.INVALID_REQUEST_ARGUMENT);
        }
    }
}
