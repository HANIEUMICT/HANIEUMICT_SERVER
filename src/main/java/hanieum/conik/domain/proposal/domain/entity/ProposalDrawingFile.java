package hanieum.conik.domain.proposal.domain.entity;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.domain.project.enumerate.FileStatus;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProposalDrawingFile extends AbstractEntity {
    @Column(nullable = false)
    private Long proposalId;

    @Column(nullable = false)
    private String drawingUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileStatus uploadStatus;

    public static ProposalDrawingFile create(ProposalDrawingUploadRequest request) {
        ProposalDrawingFile projectDrawingFile = new ProposalDrawingFile();
        projectDrawingFile.proposalId = request.proposalId();
        projectDrawingFile.drawingUrl = request.drawingUrl();
        projectDrawingFile.uploadStatus = FileStatus.TEMPORARY;
        return projectDrawingFile;
    }

    public void updateUploadStatus() {
        this.uploadStatus = FileStatus.FINALIZED;
    }
}
