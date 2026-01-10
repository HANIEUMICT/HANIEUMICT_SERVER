package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealMassProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealMassProductionRepository extends JpaRepository<DealMassProduction, Long> {
    Optional<DealMassProduction> findByDealId(Long dealId);
}
