package hanieum.conik.application.proposal;

import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.required.ProposalRepository;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.exception.ProposalErrorType;
import hanieum.conik.domain.proposal.exception.ProposalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalQueryService implements ProposalFinder {
    private final ProposalRepository proposalRepository;

    @Override
    public Proposal findProposal(Long proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ProposalException(ProposalErrorType.PROPOSAL_NOT_FOUND));
    }
}
