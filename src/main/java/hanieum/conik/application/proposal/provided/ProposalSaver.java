package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.ProposalResponse;

public interface ProposalSaver {
    ProposalResponse initiate(Long memberId);

    ProposalResponse saveProposalDraft(Long proposalId, ProposalRegisterRequest proposalRegisterRequest);

    ProposalResponse saveProposalFinal(Long proposalId, ProposalRegisterRequest proposalRegisterRequest);
}
