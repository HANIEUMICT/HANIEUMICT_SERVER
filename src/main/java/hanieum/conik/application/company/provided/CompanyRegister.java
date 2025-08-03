package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import jakarta.validation.Valid;

/**
 * 기업 등록을 구현한다.
 */
public interface CompanyRegister {
    Long register(CompanyRegisterRequest request);
}
