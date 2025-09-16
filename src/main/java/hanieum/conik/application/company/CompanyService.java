package hanieum.conik.application.company;

import hanieum.conik.adapter.company.webapi.response.CompanyDetailResponse;
import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanyRegister;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.company.required.EquipmentRepository;
import hanieum.conik.application.company.required.PortfolioRepository;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.company.dto.*;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyDetail;
import hanieum.conik.domain.company.entity.Equipment;
import hanieum.conik.domain.company.entity.Portfolio;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CompanyService implements CompanyFinder, CompanyRegister {
    private final MemberFinder memberFinder;
    private final CompanyRepository companyRepository;
    private final EquipmentRepository equipmentRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CompanySummaryResponse> findAllCompanySummaries(Pageable pageable) {
        validatePageable(pageable);

        Page<Company> page = companyRepository.findAll(pageable);
        return page.map(CompanySummaryResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailResponse findMyCompanyWithDetail(Long memberId){
        Member member = memberFinder.findById(memberId);

        Company company = companyRepository.findById(member.getCompanyId())
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));

        if (company.getCompanyDetail() == null) {
            return CompanyDetailResponse.fromCompanyOnly(company);
        }

        List<Equipment> equipments = equipmentRepository.findByCompanyDetailId(company.getId());
        List<Portfolio> portfolios = portfolioRepository.findByCompanyDetailId(company.getId());

        try {
            return CompanyDetailResponse.from(company, equipments, portfolios);
        } catch (RuntimeException e) {
            throw new CompanyException(CompanyErrorType.MAPPING_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findCompaniesByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return companyRepository.findByIdIn(ids);
    }

    @Override
    public Long register(CompanyRegisterRequest request) {
        Company company = Company.register(request);

        checkDuplicateEmail(request);

        companyRepository.save(company);
        log.info("기업 등록 성공: company id = {}", company.getId());

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
    @Transactional(readOnly = true)
    public CompanyDetailResponse findCompanyWithDetail(Long companyId) {
        Company company = companyRepository.findWithDetailById(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));

        if (company.getCompanyDetail() == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_NOT_FOUND);
        }

        List<Equipment> equipments = equipmentRepository.findByCompanyDetailId(companyId);
        List<Portfolio> portfolios = portfolioRepository.findByCompanyDetailId(companyId);

        try {
            return CompanyDetailResponse.from(company, equipments, portfolios);
        } catch (RuntimeException e) {
            throw new CompanyException(CompanyErrorType.MAPPING_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyProfileResponse> findAllCompanyWithFilter(CompanyProfileSearchCondition cond, Pageable pageable) {
        validatePageable(pageable);

        return companyRepository.findCompaniesWithFilter(cond, pageable);
    }

    private void checkDuplicateEmail(CompanyRegisterRequest request){
        if (companyRepository.findByEmail(new Email(request.email())).isPresent()) {
            throw new CompanyException(CompanyErrorType.EMAIL_DUPLICATE);
        }
    }

    private void validatePageable(Pageable p) {
        if (p.getPageNumber() < 0 || p.getPageSize() <= 0 || p.getPageSize() > 200) {
            throw new CompanyException(CompanyErrorType.INVALID_PAGINATION);
        }
    }
}
