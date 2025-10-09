package hanieum.conik.application.company;

import hanieum.conik.adapter.company.webapi.response.CompanyDetailResponse;
import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.company.required.EquipmentRepository;
import hanieum.conik.application.company.required.PortfolioRepository;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.Equipment;
import hanieum.conik.domain.company.entity.Portfolio;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyFinderService implements CompanyFinder {
    private final MemberFinder memberFinder;
    private final CompanyRepository companyRepository;
    private final EquipmentRepository equipmentRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public Page<CompanySummaryResponse> findAllCompanySummaries(Pageable pageable) {
        validatePageable(pageable);

        Page<Company> page = companyRepository.findAll(pageable);
        return page.map(CompanySummaryResponse::from);
    }

    @Override
    public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));
    }

    @Override
    public Page<CompanySummaryResponse> searchCompanySummaries(CompanySummarySearchCondition cond, Pageable pageable) {
        validatePageable(pageable);
        Page<Company> search = companyRepository.search(cond, pageable);
        return search.map(CompanySummaryResponse::from);
    }

    @Override
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
    public List<Company> findCompaniesByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return companyRepository.findByIdIn(ids);
    }

    @Override
    public CompanyDetailResponse findCompanyWithDetail(Long companyId) {
        Company company = companyRepository.findById(companyId)
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
    public Page<CompanyProfileResponse> findAllCompanyWithFilter(CompanyProfileSearchCondition cond, Pageable pageable) {
        validatePageable(pageable);

        return companyRepository.findCompaniesWithFilter(cond, pageable);
    }

    private void validatePageable(Pageable p) {
        if (p.getPageNumber() < 0 || p.getPageSize() <= 0 || p.getPageSize() > 200) {
            throw new CompanyException(CompanyErrorType.INVALID_PAGINATION);
        }
    }
}
