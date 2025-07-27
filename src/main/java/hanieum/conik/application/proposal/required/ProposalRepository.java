package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByCompanyId(Long companyId);
    List<Proposal> findByCompanyIdAndSubmitStatus(Long companyId, SubmitStatus submitStatus);
    List<Proposal> findByCompanyIdAndSubmitStatusIn(Long companyId, List<SubmitStatus> submitStatuses);
}
