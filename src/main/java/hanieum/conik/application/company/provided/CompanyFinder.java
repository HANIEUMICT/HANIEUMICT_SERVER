package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.Company;

import java.util.List;
import java.util.Optional;

/**
 * 기업 조회 기능(Port)을 정의한다.
 */
public interface CompanyFinder {
    List<Company> findAllCompanies();
    Company findCompanyById(Long companyId);
}
