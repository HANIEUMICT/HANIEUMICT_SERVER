package hanieum.conik.application.proposal.provided;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProposalFinder {
    Proposal findProposal(Long proposalId);

    Proposal getProposalDetail(Long proposalId);

    Page<Proposal> getCompanyProposals(Long memberId, Long projectId, SubmitStatus submitStatus, Pageable pageable);
}
