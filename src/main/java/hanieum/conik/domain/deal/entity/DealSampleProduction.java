package hanieum.conik.domain.deal.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealSampleProduction extends AbstractEntity { // 3. 샘플 제작 단계

    private Long dealId;
    private LocalDate startDate;
    private LocalDate endDate;

    @ElementCollection
    private List<String> imageUrls;

}
