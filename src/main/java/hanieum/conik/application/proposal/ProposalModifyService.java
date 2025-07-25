package hanieum.conik.application.proposal;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalInitiateRequest;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.application.proposal.required.ProposalDrawingFileRepository;
import hanieum.conik.application.proposal.required.ProposalRepository;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.entity.ProposalDrawingFile;
import hanieum.conik.domain.proposal.exception.ProposalErrorType;
import hanieum.conik.domain.proposal.exception.ProposalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProposalModifyService implements ProposalSaver, ProposalDrawingSaver {
    private final ProposalDrawingFileRepository proposalDrawingFileRepository;
    private final ProposalFinder proposalFinder;
    private final ProposalRepository proposalRepository;

    @Override
    public Long initiate(ProposalInitiateRequest proposalInitiateRequest) {
        // 멤버 아이디로 받았지만, 멤버가 소속된 회사 아이디를 찾고 그 아이디를 아래 빨간 컴패니 저기에 넣어야함
        try {
            Proposal proposal = Proposal.initiate(proposalInitiateRequest);
            proposalRepository.save(proposal);
            return proposal.getId();
        } catch (Exception e) {
            throw new ProposalException(ProposalErrorType.PROPOSAL_INITIATE_ERROR);
        }
    }

    @Override
    public ProjectRegisterRequest saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest) {
        return null;
    }

    @Override
    public ProjectRegisterRequest saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest) {
        return null;
    }

    @Override
    public void saveDrawingFileTemp(ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        Proposal proposal = proposalFinder.findProposal(proposalDrawingUploadRequest.proposalId());
        ProposalDrawingFile drawingFile = ProposalDrawingFile.create(proposalDrawingUploadRequest);

        proposal.addDrawing(drawingFile);

        proposalDrawingFileRepository.save(drawingFile);
    }
}
