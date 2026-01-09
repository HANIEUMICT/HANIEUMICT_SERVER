package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.proposal.dto.response.ProposalDetailResponse;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProposalFinder {
    Proposal findProposal(Long proposalId);

    List<Proposal> findSubmittedProposalsByProjectId(Long projectId);

    Page<Proposal> getCompanyProposals(Long memberId, Long projectId, SubmitStatus submitStatus, Pageable pageable);

    Page<ProposalDetailResponse> getCompanyProposals(Long memberId, Pageable pageable);
}
