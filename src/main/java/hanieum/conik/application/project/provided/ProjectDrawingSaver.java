package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;

public interface ProjectDrawingSaver {
    void saveDrawingFileTemp(ProjectDrawingUploadRequest projectDrawingUploadRequest);

    void updateDrawingFileToFinal(ProjectDrawingUploadRequest projectDrawingUploadRequest);
}
