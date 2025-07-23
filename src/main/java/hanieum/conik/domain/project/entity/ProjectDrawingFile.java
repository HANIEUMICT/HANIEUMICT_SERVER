package hanieum.conik.domain.project.entity;

import hanieum.conik.adapter.project.dto.ProjectDrawingUploadRequest;
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
public class ProjectDrawingFile extends AbstractEntity {
    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String drawingUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileStatus uploadStatus;

    public static ProjectDrawingFile create(ProjectDrawingUploadRequest request) {
        ProjectDrawingFile projectDrawingFile = new ProjectDrawingFile();
        projectDrawingFile.projectId = request.projectId();
        projectDrawingFile.drawingUrl = request.drawingUrl();
        projectDrawingFile.uploadStatus = FileStatus.TEMPORARY;
        return projectDrawingFile;
    }

    public void updateUploadStatus() {
        this.uploadStatus = FileStatus.FINALIZED;
    }
}
