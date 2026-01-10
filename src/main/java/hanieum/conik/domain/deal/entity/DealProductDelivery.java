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
public class DealProductDelivery extends AbstractEntity {

    private Long dealId;
    private LocalDateTime startedAt;

}
