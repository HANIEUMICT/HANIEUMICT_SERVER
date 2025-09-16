package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.CompanyDetailRequest;

/**
 * 기업 상세 등록을 구현한다.
 */
public interface CompanyDetailRegister {
    Long register(CompanyDetailRequest request);
}
