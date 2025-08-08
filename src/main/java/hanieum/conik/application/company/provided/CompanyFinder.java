package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.Company;

import java.util.List;

/**
 * 기업 조회 기능(Port)을 정의한다.
 */
public interface CompanyFinder {
    List<Company> findAllCompanies();

    Company findCompany(Long companyId);

    Company findCompanyWithAddresses(Long companyId);
}
