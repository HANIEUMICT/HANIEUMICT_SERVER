package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByCompanyId(Long companyId);

    Page<Proposal> findByCompanyIdAndSubmitStatusIn(Long companyId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Proposal> findByCompanyIdAndSubmitStatus(Long companyId, SubmitStatus submitStatus, Pageable pageable);

    Page<Proposal> findByCompanyIdAndProjectIdAndSubmitStatusIn(Long companyId, Long projectId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Proposal> findByCompanyIdAndProjectIdAndSubmitStatus(Long companyId, Long projectId, SubmitStatus submitStatus, Pageable pageable);
}
