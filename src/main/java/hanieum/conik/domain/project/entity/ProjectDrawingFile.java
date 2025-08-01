package hanieum.conik.domain.project.entity;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.domain.project.enumerate.FileStatus;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectDrawingFile extends AbstractEntity {
    private Project project;

    private String drawingUrl;

    private FileStatus uploadStatus;

    public static ProjectDrawingFile create(Project project, ProjectDrawingUploadRequest request) {
        ProjectDrawingFile projectDrawingFile = new ProjectDrawingFile();
        projectDrawingFile.project = project;
        projectDrawingFile.drawingUrl = request.drawingUrl();
        projectDrawingFile.uploadStatus = FileStatus.TEMPORARY;
        return projectDrawingFile;
    }

    public void updateUploadStatus() {
        this.uploadStatus = FileStatus.FINALIZED;
    }

    public void updateProject(Project project) {
        this.project = project;
    }
}
