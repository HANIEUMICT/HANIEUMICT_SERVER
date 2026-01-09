package hanieum.conik.application.company.required;

import hanieum.conik.adapter.company.response.CompanyProfileResponse;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<CompanyProfileResponse> findCompaniesWithFilter(CompanyProfileSearchCondition cond, Pageable pageable);
    Page<Company> search(CompanySummarySearchCondition cond, Pageable pageable);
}