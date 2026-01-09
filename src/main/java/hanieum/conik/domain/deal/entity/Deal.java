package hanieum.conik.domain.deal.entity;

import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.deal.enumerate.DeliveryStatus;
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

    @Column(nullable = false)
    private Long buyerId; // 거래하는 소상공인 아이디

    @Column(nullable = false)
    private Long companyId; // 공급하는 공장 아이디

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DealStep dealStep;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public static Deal create(Long projectId, Long companyId, DealStep step) {
        Deal progress = new Deal();
        progress.projectId = projectId;
        progress.companyId = companyId;
        progress.dealStep = step;
        return progress;
    }
}
