package hanieum.conik.application.company.provided;

import hanieum.conik.adapter.company.webapi.response.CompanyDetailResponse;
import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * 기업 조회 기능(Port)을 정의한다.
 */
public interface CompanyFinder {
    Page<CompanySummaryResponse> findAllCompanySummaries(Pageable pageable);

    Company findCompany(Long companyId);

    Page<CompanySummaryResponse> searchCompanySummaries(CompanySummarySearchCondition cond, Pageable pageable);

    List<Company> findCompaniesByIds(Collection<Long> ids);

    CompanyDetailResponse findCompanyWithDetail(Long companyId);

    Page<CompanyProfileResponse> findAllCompanyWithFilter(CompanyProfileSearchCondition cond, Pageable pageable);

    List<CompanyProfileResponse> findRecommendedCompanies();
}
