package hanieum.conik.adapter.company.webapi;

import hanieum.conik.adapter.company.webapi.response.CompanyResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanyRegister;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/company")
@Tag(name = "COMPANY", description = "기업 관련 API")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyFinder companyFinder;
    private final CompanyRegister companyRegister;

    @Operation(summary = "기업 등록", description = """
    ## 기업 등록을 수행합니다.
    - 기업 정보를 모두 기입한 후에 기업 등록을 진행합니다.
    - 기업 등록 완료시 기업 id를 반환 받습니다.
    """)
    @PostMapping()
    public ApiResponse<Long> registerCompany(@Valid @RequestBody CompanyRegisterRequest request) {
        return ApiResponse.success(companyRegister.register(request));
    }

    @Operation(summary = "전체 기업 summary 조회", description = """
    ## 기업 회원 가입 시 전체 기업 조회를 수행합니다.
    - 등록되어있는 모든 기업을 조회할 수 있습니다.
    - 기업명, 업종, 대표자, 사업자 등록번호, 주소 정보를 확인할 수 있습니다. 
    """)
    @GetMapping("/summaries")
    public ApiResponse<Page<CompanySummaryResponse>> findAllCompaniesSummary(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(companyFinder.findAllCompanySummaries(pageable));
    }

    @Operation(summary = "특정 기업 조회", description = """
    ## 특정 기업 조회를 수행합니다.
    - 기업id를 통해 특정 기업을 조회할 수 있습니다.
    """)
    @GetMapping("/{companyId}")
    public ApiResponse<CompanyResponse> findCompany(@PathVariable Long companyId) {
        Company company = companyFinder.findCompanyWithAddresses(companyId);
        return ApiResponse.success(CompanyResponse.from(company));
    }
}
