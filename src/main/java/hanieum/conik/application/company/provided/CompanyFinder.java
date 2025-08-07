package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 기업 조회 기능(Port)을 정의한다.
 */
public interface CompanyFinder {
    Page<Company> findAllCompanies(Pageable pageable);
    Company findCompany(Long companyId);
}
