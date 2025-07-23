package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.ProjectRegisterRequest;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hanieum.conik.application.project.required.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectModifyService implements ProjectSaver {
    private final ProjectRepository projectRepository;
    private final ProjectFinder projectFinder;

    public Long initiate(Long memberId) {
        try {
            Project project = Project.create(memberId);
            projectRepository.save(project);
            return project.getId();
        } catch (Exception e) {
            throw new ProjectException(ProjectErrorType.PROJECT_SAVE_ERROR);
        }
    }

    @Override
    public ProjectRegisterRequest saveProjectDraft(Long memberId, ProjectRegisterRequest projectRegisterRequest) {
        try{
            Project project = projectFinder.findProject(memberId);
            project.updateDraft(projectRegisterRequest);
            projectRepository.save(project);
            return ProjectRegisterRequest.from(project);
        }
        catch (Exception e) {
            throw new ProjectException(ProjectErrorType.PROJECT_SAVE_ERROR);
        }
    }

    @Override
    public Long saveProjectFinal(Long memberId, ProjectRegisterRequest projectRegisterRequest) {
        return 0L;
    }


}
