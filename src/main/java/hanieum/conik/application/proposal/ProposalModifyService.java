package hanieum.conik.application.proposal;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.ProposalResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.application.proposal.required.ProposalDrawingFileRepository;
import hanieum.conik.application.proposal.required.ProposalRepository;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.entity.ProposalDrawingFile;
import hanieum.conik.domain.proposal.domain.enumerate.ProposalBidStatus;
import hanieum.conik.domain.proposal.exception.ProposalErrorType;
import hanieum.conik.domain.proposal.exception.ProposalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class ProposalModifyService implements ProposalSaver, ProposalDrawingSaver {
    private final ProposalDrawingFileRepository proposalDrawingFileRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalFinder proposalFinder;
    private final ProjectFinder projectFinder;
    private final MemberFinder memberFinder;

    private final Map<SubmitStatus, Consumer<Proposal>> statusHandlers = Map.of(
            SubmitStatus.TEMPORARY_SAVE, Proposal::updateToDraft,
            SubmitStatus.SUBMIT, Proposal::updateToFinal
    );

    @Override
    public Proposal initiate(Long memberId, Long projectId) {
        try {
            Project project = projectFinder.validateProjectOpenStatus(projectId);

            Proposal proposal = Proposal.initiate(memberFinder.find(memberId), project);

            return proposalRepository.save(proposal);
        } catch (Exception e) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_INITIATE_ERROR);
        }
    }

    @Override
    public Proposal saveProposalDraft(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        if (!proposalRegisterRequest.submitStatus().equals(SubmitStatus.TEMPORARY_SAVE)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DRAFT_REQUEST_ERROR);
        }
        return getSavedProposal(proposalId, proposalRegisterRequest);
    }

    @Override
    public Proposal saveProposalFinal(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        if (!proposalRegisterRequest.submitStatus().equals(SubmitStatus.SUBMIT)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_FINAL_REQUEST_ERROR);
        }
        return getSavedProposal(proposalId, proposalRegisterRequest);
    }

    private Proposal getSavedProposal(Long proposalId, ProposalRegisterRequest request) {
        try {
            Proposal proposal = proposalFinder.findProposal(proposalId);
            proposal.update(request);

            Consumer<Proposal> handler = statusHandlers.get(request.submitStatus());
            handler.accept(proposal);

            return proposal;
        } catch (Exception e) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_SAVE_ERROR);
        }
    }

    @Override
    public void saveDrawingFileTemp(ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        Proposal proposal = proposalFinder.findProposal(proposalDrawingUploadRequest.proposalId());
        ProposalDrawingFile drawingFile = ProposalDrawingFile.create(proposalDrawingUploadRequest);

        proposal.addDrawing(drawingFile);

        proposalDrawingFileRepository.save(drawingFile);
    }

    @Override
    public Proposal updateToDealRequested(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!proposalBidStatus.equals(ProposalBidStatus.DEAL_REQUESTED)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_REQUEST_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.updateToBidRequested();
        return proposal;
    }

    @Override
    public Proposal acceptDeal(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!ProposalBidStatus.ACCEPT_DEAL.equals(proposalBidStatus)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_ACCEPT_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.acceptDeal();
        /** 거래 상태 레코드 생성 로직 추가**/
        return proposal;
    }

    @Override
    public Proposal rejectDeal(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!ProposalBidStatus.REJECT_DEAL.equals(proposalBidStatus)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_REJECT_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.rejectDeal();
        return proposal;
    }
}
