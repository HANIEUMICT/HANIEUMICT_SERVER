package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealContractRepository extends JpaRepository<DealContract, Long> {
    Optional<DealContract> findByDealId(Long dealId);
}

