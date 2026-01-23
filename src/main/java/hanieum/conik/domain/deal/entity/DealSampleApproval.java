package hanieum.conik.domain.deal.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealSampleApproval extends AbstractEntity { // 5. 샘플 승인 단계

    private Long dealId;
    private boolean approved;
    private String documentUrl;

}

