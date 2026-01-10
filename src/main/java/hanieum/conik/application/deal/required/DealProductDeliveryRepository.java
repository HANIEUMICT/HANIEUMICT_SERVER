package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealProductDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealProductDeliveryRepository extends JpaRepository<DealProductDelivery, Long> {
    Optional<DealProductDelivery> findByDealId(Long dealId);
}
