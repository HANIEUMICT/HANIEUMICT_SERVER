package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;

public interface ProposalDrawingSaver {
    void saveDrawingFileTemp(ProposalDrawingUploadRequest proposalDrawingUploadRequest);
}
