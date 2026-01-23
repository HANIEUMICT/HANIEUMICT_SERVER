package hanieum.conik.domain.deal.entity;

import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.deal.enumerate.DeliveryStatus;
import hanieum.conik.domain.deal.exception.DealErrorType;
import hanieum.conik.domain.deal.exception.DealException;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal extends AbstractEntity {
    @Column(nullable = false)
    private Long projectId;

    private Long buyerId; // 거래하는 소상공인 아이디

    private Long companyId; // 공급하는 공장 아이디

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DealStep dealStep;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    // 생성 (Project 최종 제출 시)
    public static Deal open(Long projectId, Long buyerId) {
        Deal deal = new Deal();
        deal.projectId = projectId;
        deal.buyerId = buyerId;
        deal.dealStep = DealStep.OPEN;
        return deal;
    }

    // 요청 상태로 변경 (Proposal 최종 제출 시)
    public void request() {
        if (this.dealStep != DealStep.OPEN) {
            throw new DealException(DealErrorType.INVALID_DEAL_STEP);
        }
        this.dealStep = DealStep.REQUESTED;
    }
}
