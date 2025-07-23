package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.proposal.domain.entity.ProposalItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalItemRepository extends JpaRepository<ProposalItem, Long> {
}
