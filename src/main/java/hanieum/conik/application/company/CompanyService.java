package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanyRegister;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.global.application.required.BucketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CompanyService implements CompanyFinder, CompanyRegister {

    private final BucketClient bucketClient;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Company findCompanyWithAddresses(Long companyId) {
        return companyRepository.findByIdWithAddresses(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Override
    public Company findCompanyWithDetail(Long companyId) {
        return companyRepository.findByIdWithDetail(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Override
    public Long register(CompanyRegisterRequest request) {

        Company company = Company.register(request);

        companyRepository.save(company);
        log.info("기업 등록 성공: company id = {}", company.getId());

        return company.getId();
    }
}
