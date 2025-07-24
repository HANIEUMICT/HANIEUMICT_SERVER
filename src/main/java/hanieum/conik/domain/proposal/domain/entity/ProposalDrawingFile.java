package hanieum.conik.domain.proposal.domain.entity;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.domain.project.enumerate.FileStatus;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProposalDrawingFile extends AbstractEntity {
    private Proposal proposal;

    private String drawingUrl;

    private FileStatus uploadStatus;

    public static ProposalDrawingFile create(ProposalDrawingUploadRequest request) {
        ProposalDrawingFile projectDrawingFile = new ProposalDrawingFile();
        projectDrawingFile.drawingUrl = request.drawingUrl();
        projectDrawingFile.uploadStatus = FileStatus.TEMPORARY;
        return projectDrawingFile;
    }

    public void updateUploadStatus() {
        this.uploadStatus = FileStatus.FINALIZED;
    }
}
