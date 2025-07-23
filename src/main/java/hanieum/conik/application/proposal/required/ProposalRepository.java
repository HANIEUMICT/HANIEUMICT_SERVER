package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
