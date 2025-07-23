package hanieum.conik.application.project.provided;

import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import java.util.List;

public interface ProjectDrawingFinder {
    List<ProjectDrawingFile> findProjectDrawingFiles(Long projectId);
}
