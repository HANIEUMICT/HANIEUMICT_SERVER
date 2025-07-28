package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.proposal.dto.response.ProposalDetailResponse;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;

import java.util.List;

public interface ProposalFinder {
    Proposal findProposal(Long proposalId);

    List<ProposalDetailResponse> getCompanyProposals(Long memberId, Long projectId, SubmitStatus submitStatus);
}
