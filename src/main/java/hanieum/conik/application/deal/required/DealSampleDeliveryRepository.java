package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealSampleDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealSampleDeliveryRepository extends JpaRepository<DealSampleDelivery, Long> {
    Optional<DealSampleDelivery> findByDealId(Long dealId);
}
