package hanieum.conik.application.company.required;

import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<CompanyProfileResponse> findCompaniesWithFilter(CompanyProfileSearchCondition cond, Pageable pageable);
}