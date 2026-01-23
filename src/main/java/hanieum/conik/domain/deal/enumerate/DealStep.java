package hanieum.conik.domain.deal.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DealStep {

    OPEN("거래 오픈", 0),
    REQUESTED("거래 요청", 0),

    CONTRACT_CONFIRMED("거래 요청 수락", 1),

    COMPANY_INSPECTION_COMPLETED("공장 검수 완료", 2),

    SAMPLE_PRODUCTION("샘플 제작 중", 3),
    SAMPLE_PRODUCTION_COMPLETED("샘플 제작 완료", 3),

    SAMPLE_DELIVERY("샘플 배송 중", 4),
    SAMPLE_DELIVERED("샘플 배송 완료", 4),

    SAMPLE_APPROVED("샘플 승인", 5),
    SAMPLE_REJECTED("샘플 반려", 5),

    MASS_PRODUCTION("본 제작 중", 6),
    MASS_PRODUCTION_COMPLETED("본 제작 완료", 6),

    PRODUCT_DELIVERY("제작물 배송 중", 7),

    CLOSED("거래 종료", 8);

    private final String description;
    private final int step;

}
