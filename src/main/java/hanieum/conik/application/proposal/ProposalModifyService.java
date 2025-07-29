package hanieum.conik.application.proposal;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.ProposalResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.application.proposal.required.ProposalDrawingFileRepository;
import hanieum.conik.application.proposal.required.ProposalRepository;
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
    private final ProposalFinder proposalFinder;
    private final ProposalRepository proposalRepository;
    private final MemberFinder memberFinder;

    private final Map<SubmitStatus, Consumer<Proposal>> statusHandlers = Map.of(
            SubmitStatus.TEMPORARY_SAVE, Proposal::updateToDraft,
            SubmitStatus.SUBMIT, Proposal::updateToFinal
    );

    @Override
    public ProposalResponse initiate(Long memberId) {
        try {
            Proposal proposal = Proposal.initiate(memberFinder.find(memberId));
            proposalRepository.save(proposal);
            return ProposalResponse.from(proposal.getId(), ProposalRegisterRequest.from(proposal));
        } catch (Exception e) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_INITIATE_ERROR);
        }
    }

    @Override
    public ProposalResponse saveProposalDraft(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        if (!proposalRegisterRequest.submitStatus().equals(SubmitStatus.TEMPORARY_SAVE)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DRAFT_REQUEST_ERROR);
        }
        return getSavedProposal(proposalId, proposalRegisterRequest);
    }

    @Override
    public ProposalResponse saveProposalFinal(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        if (!proposalRegisterRequest.submitStatus().equals(SubmitStatus.SUBMIT)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_FINAL_REQUEST_ERROR);
        }
        return getSavedProposal(proposalId, proposalRegisterRequest);
    }

    private ProposalResponse getSavedProposal(Long proposalId, ProposalRegisterRequest request) {
        try {
            Proposal proposal = proposalFinder.findProposal(proposalId);
            proposal.update(request);

            Consumer<Proposal> handler = statusHandlers.get(request.submitStatus());
            handler.accept(proposal);

            proposalRepository.save(proposal);
            return ProposalResponse.from(proposal.getId(), ProposalRegisterRequest.from(proposal));
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
    public ProposalResponse updateToDealRequested(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!proposalBidStatus.equals(ProposalBidStatus.DEAL_REQUESTED)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_REQUEST_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.updateToBidRequested();
        return ProposalResponse.from(proposal.getId(), ProposalRegisterRequest.from(proposal));
    }

    @Override
    public ProposalResponse AcceptDeal(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!proposalBidStatus.equals(ProposalBidStatus.ACCEPT_DEAL)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_ACCEPT_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.acceptDeal();
        /** 거래 상태 레코드 생성 로직 추가**/
        return ProposalResponse.from(proposal.getId(), ProposalRegisterRequest.from(proposal));
    }

    @Override
    public ProposalResponse RejectDeal(Long proposalId, ProposalBidStatus proposalBidStatus) {
        if (!proposalBidStatus.equals(ProposalBidStatus.REJECT_DEAL)) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_DEAL_REJECT_ERROR);
        }
        Proposal proposal = proposalFinder.findProposal(proposalId);
        proposal.rejectDeal();
        return ProposalResponse.from(proposal.getId(), ProposalRegisterRequest.from(proposal));
    }
}
