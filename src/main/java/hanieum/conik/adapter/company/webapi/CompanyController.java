package hanieum.conik.adapter.company.webapi;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanyRegister;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "전체 기업 조회", description = """
    ## 전체 기업 조회를 수행합니다.
    - 등록되어있는 모든 기업을 조회할 수 있습니다.
    """)
    @GetMapping
    public ApiResponse<List<Company>> findAllCompanies() {
        return ApiResponse.success(companyFinder.findAllCompanies());
    }

    @Operation(summary = "특정 기업 조회", description = """
    ## 특정 기업 조회를 수행합니다.
    - 기업id를 통해 특정 기업을 조회할 수 있습니다.
    """)
    @GetMapping("/{companyId}")
    public ApiResponse<Company> findCompany(@PathVariable Long companyId) {
        return ApiResponse.success(companyFinder.findCompany(companyId));
    }
}
