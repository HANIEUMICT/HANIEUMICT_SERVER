package hanieum.conik.domain.deal.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealContract extends AbstractEntity { // 1. 거래 승인 단계

    private Long dealId;
    private LocalDateTime confirmedAt;

}
