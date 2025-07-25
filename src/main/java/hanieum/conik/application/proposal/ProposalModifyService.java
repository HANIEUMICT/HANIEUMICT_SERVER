package hanieum.conik.application.proposal;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.application.proposal.required.ProposalDrawingFileRepository;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.entity.ProposalDrawingFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProposalModifyService implements ProposalSaver, ProposalDrawingSaver {
    private final ProposalDrawingFileRepository proposalDrawingFileRepository;
    private final ProposalFinder proposalFinder;

    @Override
    public void saveDrawingFileTemp(ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        Proposal proposal = proposalFinder.findProposal(proposalDrawingUploadRequest.proposalId());
        ProposalDrawingFile drawingFile = ProposalDrawingFile.create(proposalDrawingUploadRequest);

        proposal.addDrawing(drawingFile);

        proposalDrawingFileRepository.save(drawingFile);
    }
}
