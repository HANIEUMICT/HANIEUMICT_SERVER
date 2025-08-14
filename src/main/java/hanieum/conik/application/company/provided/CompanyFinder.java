package hanieum.conik.application.company.provided;

import hanieum.conik.adapter.company.webapi.response.CompanyDetailResponse;
import hanieum.conik.domain.company.Company;

import java.util.Collection;
import java.util.List;

/**
 * 기업 조회 기능(Port)을 정의한다.
 */
public interface CompanyFinder {
    List<CompanyDetailResponse> findAllCompanies();

    Company findCompany(Long companyId);

    Company findCompanyWithDetail(Long companyId);

    Company findCompanyWithAddresses(Long companyId);

    List<Company> findCompaniesWithDetail(Collection<Long> ids);
}
