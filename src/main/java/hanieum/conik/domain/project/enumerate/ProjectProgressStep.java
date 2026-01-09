package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectProgressStep {
    OPEN("오픈"),
    REQUESTED("거래 요청"),
    CONTRACT_COMPLETED("거래 요청 수락"),
    SAMPLING("샘플 제작 중"),
    SAMPLING_COMPLETED("샘플 제작 완료"),
    SAMPLE_IN_DELIVERY("샘플 배송 중"),
    IN_PRODUCTION("제작 중"),
    PRODUCTION_COMPLETED("제작 완료"),
    PRODUCTION_IN_DELIVERY("제작물 배송 중"),
    CLOSED("거래 CLOSE");

    private final String description;
}
