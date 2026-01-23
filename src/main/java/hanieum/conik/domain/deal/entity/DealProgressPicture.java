package hanieum.conik.domain.deal.entity;

import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealProgressPicture extends AbstractEntity {
    private Long dealId;

    private DealStep dealStep;

    private String pictureUrl; // TODO: 리스트 가능하게 수정
}
