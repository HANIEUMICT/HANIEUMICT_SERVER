package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanyRegister;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.adapter.s3.dto.ImageUploadRequest;
import hanieum.conik.global.adapter.s3.dto.ReadPreSignedUrlResponse;
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
    public Company findCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Override
    public Long register(CompanyRegisterRequest request) {

        Company company = createCompanyWithFileUrls(request);

        companyRepository.save(company);
        log.info("기업 등록 성공: company id = {}", company.getId());

        return company.getId();
    }

    private Company createCompanyWithFileUrls(CompanyRegisterRequest request) {
        ReadPreSignedUrlResponse certUrls = bucketClient.getPreSignedUrl(new ImageUploadRequest("certificates", request.registrationCertificateUrl().getOriginalFilename()));
        ReadPreSignedUrlResponse bankUrls = bucketClient.getPreSignedUrl(new ImageUploadRequest("bankbooks", request.bankbookCopy().getOriginalFilename()));
        ReadPreSignedUrlResponse profUrls = bucketClient.getPreSignedUrl(new ImageUploadRequest("profiles", request.profileUrl().getOriginalFilename()));

        return Company.register(
                request.name(),
                request.owner(),
                new Email(request.email()),
                request.phoneNumber(),
                request.businessType(),
                request.industry(),
                request.registrationNumber(),
                certUrls.objectUrl(),
                profUrls.objectUrl(),
                bankUrls.objectUrl()
        );
    }
}
