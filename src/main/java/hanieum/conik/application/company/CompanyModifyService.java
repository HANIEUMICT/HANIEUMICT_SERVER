package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanySaver;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.domain.company.dto.CompanyDetailCreateRequest;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.dto.CompanyUpdateRequest;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyDetail;
import hanieum.conik.domain.company.entity.Equipment;
import hanieum.conik.domain.company.entity.Portfolio;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyModifyService implements CompanySaver {
    private final MemberFinder memberFinder;
    private final CompanyFinder companyFinder;
    private final CompanyRepository companyRepository;

    @Override
    public Long register(CompanyRegisterRequest request) {
        Company company = Company.register(request);

        checkDuplicateEmail(new Email(request.email()));

        companyRepository.save(company);

        return company.getId();
    }

    @Override
    public Long registerCompanyDetail(Long memberId, CompanyDetailCreateRequest request) {
        if (memberId == null || memberId <= 0) {
            throw new CompanyException(CompanyErrorType.INVALID_INPUT);
        }

        if (request == null || request.detail() == null) {
            throw new CompanyException(CompanyErrorType.INVALID_INPUT);
        }

        var detail = request.detail();
        if (detail.establishedAt() == null || detail.logoUrl() == null || detail.logoUrl().isBlank()) {
            throw new CompanyException(CompanyErrorType.INVALID_INPUT);
        }

        var member = memberFinder.findById(memberId);
        Long companyId = member.getCompanyId();
        if (companyId == null) throw new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));

        if (company.getCompanyDetail() != null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_ALREADY_EXISTS);
        }

        List<Equipment> equipments = Optional.ofNullable(request.equipments())
                .orElseGet(List::of)
                .stream().map(Equipment::create).toList();

        List<Portfolio> portfolios = Optional.ofNullable(request.portfolios())
                .orElseGet(List::of)
                .stream().map(Portfolio::create).toList();

        CompanyDetail companyDetail = CompanyDetail.create(company, request.detail(),equipments, portfolios);
        companyRepository.save(company);

        return companyDetail.getId();
    }

    @Override
    public void updateCompanyInfo(Long companyId, CompanyUpdateRequest request) {
        Company company = companyFinder.findCompany(companyId);

        if (request.email() != null) {
            Email newEmail = new Email(request.email().trim());

            if (!newEmail.equals(company.getEmail())) {
                checkDuplicateEmail(newEmail);
            }
        }
        company.update(request);
    }

    private void checkDuplicateEmail(Email email){
        if (companyRepository.findByEmail(email).isPresent()) {
            throw new CompanyException(CompanyErrorType.EMAIL_DUPLICATE);
        }
    }
}
