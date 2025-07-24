package hanieum.conik.application.project;

import hanieum.conik.application.project.provided.ProjectDrawingFinder;
import hanieum.conik.application.project.required.ProjectDrawingRepository;
import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectDrawingQueryService implements ProjectDrawingFinder {
    private final ProjectDrawingRepository projectDrawingRepository;

    @Override
    public List<ProjectDrawingFile> findProjectDrawingFiles(Long projectId) {
        return projectDrawingRepository.findAllByProjectId(projectId);
    }
}
