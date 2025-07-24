package hanieum.conik.application.project.required;

import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDrawingRepository extends JpaRepository<ProjectDrawingFile, Long> {
    List<ProjectDrawingFile> findAllByProjectId(Long projectId);
}
