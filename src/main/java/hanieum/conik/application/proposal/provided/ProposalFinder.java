package hanieum.conik.application.proposal.provided;

import hanieum.conik.domain.proposal.domain.entity.Proposal;

public interface ProposalFinder {
    Proposal findProposal(Long proposalId);
}
