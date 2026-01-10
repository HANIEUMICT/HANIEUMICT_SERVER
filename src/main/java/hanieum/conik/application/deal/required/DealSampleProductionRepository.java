package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealSampleProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealSampleProductionRepository extends JpaRepository<DealSampleProduction, Long> {
    Optional<DealSampleProduction> findByDealId(Long dealId);
}
