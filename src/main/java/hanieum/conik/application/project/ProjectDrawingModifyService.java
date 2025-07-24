package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.application.project.provided.ProjectDrawingFinder;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.project.required.ProjectDrawingRepository;
import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectDrawingModifyService implements ProjectDrawingSaver {
     private final ProjectDrawingRepository projectDrawingRepository;
     private final ProjectDrawingFinder projectDrawingFinder;

     @Override
     public void saveDrawingFileTemp(ProjectDrawingUploadRequest projectDrawingUploadRequest) {
         try{
             ProjectDrawingFile projectDrawingFile = ProjectDrawingFile.create(projectDrawingUploadRequest);
             projectDrawingRepository.save(projectDrawingFile);
         }
         catch (Exception e) {
             throw new ProjectException(ProjectErrorType.PROJECT_DRAWING_SAVE_ERROR);
         }
     }

     @Override
     public void updateDrawingFileToFinal(ProjectDrawingUploadRequest projectDrawingUploadRequest) {
         List<ProjectDrawingFile> drawingFiles = projectDrawingFinder.findProjectDrawingFiles(projectDrawingUploadRequest.projectId());
         drawingFiles.forEach(ProjectDrawingFile::updateUploadStatus);
     }
}

