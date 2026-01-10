package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealInspection;
import hanieum.conik.domain.deal.entity.DealSampleProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealInspectionRepository extends JpaRepository<DealInspection, Long> {
    Optional<DealInspection> findByDealId(Long dealId);
}
