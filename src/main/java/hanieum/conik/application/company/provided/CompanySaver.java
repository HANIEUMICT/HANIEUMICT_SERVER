package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.*;

/**
 * 기업 등록을 구현한다.
 */
public interface CompanySaver {
    Long register(CompanyRegisterRequest request);

    Long registerCompanyDetail(Long memberId, CompanyDetailCreateRequest request);

    void updateCompanyInfo(Long companyId, CompanyUpdateRequest request);
}
