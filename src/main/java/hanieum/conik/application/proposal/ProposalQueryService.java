package hanieum.conik.application.proposal;

import hanieum.conik.adapter.proposal.dto.response.ProposalDetailResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.required.ProposalRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.exception.ProposalErrorType;
import hanieum.conik.domain.proposal.exception.ProposalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalQueryService implements ProposalFinder {
    private final MemberFinder memberFinder;
    private final ProposalRepository proposalRepository;

    @Override
    public Proposal findProposal(Long proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ProposalException(ProposalErrorType.PROPOSAL_NOT_FOUND));
    }

    @Override
    public List<ProposalDetailResponse> getCompanyProposals(Long memberId, SubmitStatus submitStatus) {
        Member member = memberFinder.find(memberId);
        Long companyId = member.getCompanyId();

        List<Proposal> proposals = findProposalsWithStatus(submitStatus, companyId);

        return proposals.stream()
                .map(ProposalDetailResponse::from)
                .toList();
    }

    private List<Proposal> findProposalsWithStatus(SubmitStatus submitStatus, Long companyId) {
        return (submitStatus == null)
                ? proposalRepository.findByCompanyIdAndSubmitStatusIn(companyId, List.of(SubmitStatus.TEMPORARY_SAVE, SubmitStatus.SUBMIT))
                : proposalRepository.findByCompanyIdAndSubmitStatus(companyId, submitStatus);
    }
}
