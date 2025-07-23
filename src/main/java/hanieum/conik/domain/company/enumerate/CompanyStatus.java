package hanieum.conik.domain.company.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyStatus {
    REGISTER_PENDING("기업 등록 승인 대기"),
    REGISTER_APPROVED("기업 등록 승인 완료"),
    REGISTER_REJECTED("기업 등록 승인 거절");

    private final String description;
}
