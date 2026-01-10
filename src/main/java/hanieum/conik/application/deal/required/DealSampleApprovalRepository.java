package hanieum.conik.application.deal.required;

import hanieum.conik.domain.deal.entity.DealSampleApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealSampleApprovalRepository extends JpaRepository<DealSampleApproval, Long> {
    Optional<DealSampleApproval> findByDealId(Long dealId);
}
