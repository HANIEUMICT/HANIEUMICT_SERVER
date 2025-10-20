package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.CompanySaver;
import hanieum.conik.application.company.provided.EquipmentSaver;
import hanieum.conik.application.company.provided.PortfolioSaver;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.domain.company.dto.*;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyDetail;
import hanieum.conik.domain.company.entity.Equipment;
import hanieum.conik.domain.company.entity.Portfolio;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyModifyService implements CompanySaver {
    private final CompanyFinder companyFinder;
    private final CompanyRepository companyRepository;
    private final EquipmentSaver equipmentSaver;
    private final PortfolioSaver portfolioSaver;

    @Override
    public Long register(CompanyRegisterRequest request) {
        Company company = Company.register(request);

        checkDuplicateEmail(new Email(request.email()));

        companyRepository.save(company);

        return company.getId();
    }

    @Override
    public Long registerCompanyDetail(Long companyId, CompanyDetailCreateRequest request) {
        Company company = companyFinder.findCompany(companyId);// 회원의 회사 존재 여부 확인

        if (company.getCompanyDetail() != null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_ALREADY_EXISTS);
        }

        if (request == null || request.detail() == null) {
            throw new CompanyException(CompanyErrorType.INVALID_INPUT);
        }

        var detail = request.detail();
        if (detail.establishedAt() == null || detail.logoUrl() == null || detail.logoUrl().isBlank()) {
            throw new CompanyException(CompanyErrorType.INVALID_INPUT);
        }

        List<Equipment> equipments = registerEquipments(request);
        List<Portfolio> portfolios = registerPortfolios(request);

        CompanyDetail companyDetail = CompanyDetail.create(company, request.detail(),equipments, portfolios);
        companyRepository.save(company);

        return companyDetail.getId();
    }

    @Override
    public void update(Long companyId, CompanyUpdateRequest request) {
        Company company = companyFinder.findCompany(companyId);

        if (request.email() != null) {
            Email newEmail = new Email(request.email().trim());

            if (!newEmail.equals(company.getEmail())) {
                checkDuplicateEmail(newEmail);
            }
        }
        company.update(request);
    }

    @Override
    public void updateCompanyDetail(Long companyId, Long ifMatchEpochMilli, CompanyDetailUpdateRequest request) {
        CompanyDetail companyDetail = companyFinder.findCompanyDetail(companyId);

        if (ifMatchEpochMilli == null) throw new GlobalException(GlobalErrorType.PRECONDITION_FAILED);
        if (companyDetail.getModifiedAt() == null) throw new GlobalException(GlobalErrorType.PRECONDITION_FAILED);

        // 1) 동시성: If-Match(modifiedAt) 비교
        long current = companyDetail.getModifiedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
        if (current != ifMatchEpochMilli) {
            throw new GlobalException(GlobalErrorType.PRECONDITION_FAILED);
        }

        // 2) 부모 기본 필드 반영
        companyDetail.update(request.detail());

        // 3) 자식 컬렉션 동기화: Equipments, Portfolios
        equipmentSaver.sync(companyDetail, request.equipments());
        portfolioSaver.sync(companyDetail, request.portfolios());
    }

    private void checkDuplicateEmail(Email email){
        if (companyRepository.findByEmail(email).isPresent()) {
            throw new CompanyException(CompanyErrorType.EMAIL_DUPLICATE);
        }
    }

    @NotNull
    private static List<Portfolio> registerPortfolios(CompanyDetailCreateRequest request) {
        return Optional.ofNullable(request.portfolios())
                .orElseGet(List::of)
                .stream().map(Portfolio::create).toList();
    }

    @NotNull
    private static List<Equipment> registerEquipments(CompanyDetailCreateRequest request) {
        return Optional.ofNullable(request.equipments())
                .orElseGet(List::of)
                .stream().map(Equipment::create).toList();
    }

}
