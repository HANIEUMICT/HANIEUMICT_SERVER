package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.ProposalResponse;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.enumerate.ProposalBidStatus;

public interface ProposalSaver {
    Proposal initiate(Long memberId, Long projectId);

    Proposal saveProposalDraft(Long proposalId, ProposalRegisterRequest proposalRegisterRequest);

    Proposal saveProposalFinal(Long proposalId, ProposalRegisterRequest proposalRegisterRequest);

    Proposal updateToDealRequested(Long proposalId, ProposalBidStatus proposalBidStatus);

    Proposal rejectDeal(Long proposalId, ProposalBidStatus proposalBidStatus);

    Proposal acceptDeal(Long proposalId, ProposalBidStatus proposalBidStatus);
}
