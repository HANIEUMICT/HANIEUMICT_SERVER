package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectModifyService implements ProjectSaver {
    private final ProjectRepository projectRepository;
    private final ProjectFinder projectFinder;

    @Override
    public Long initiate(Long memberId) {
        try {
            Project project = Project.create(memberId);
            projectRepository.save(project);
            return project.getId();
        } catch (Exception e) {
            throw new ProjectException(ProjectErrorType.PROJECT_INITIATE_ERROR);
        }
    }

    @Override
    public MemberProjectQueryResponse saveProjectDraft(Long projectId, ProjectRegisterRequest request) {
        if (request.isFinalized()) {
            throw new ProjectException(ProjectErrorType.PROJECT_DRAFT_SAVE_ERROR);
        }
        return getSavedProject(projectId, request);
    }

    @Override
    public MemberProjectQueryResponse saveProjectFinal(Long projectId, ProjectRegisterRequest request) {
        if (!request.isFinalized()) {
            throw new ProjectException(ProjectErrorType.FINAL_PROJECT_SAVE_ERROR);
        }
        return getSavedProject(projectId, request);
    }

    private MemberProjectQueryResponse getSavedProject(Long projectId, ProjectRegisterRequest request) {
        try {
            Project project = projectFinder.findProject(projectId);
            project.updateDraft(request);
            projectRepository.save(project);
            return MemberProjectQueryResponse.from(projectId, ProjectRegisterRequest.from(project));
        } catch (Exception e) {
            throw new ProjectException(ProjectErrorType.PROJECT_SAVE_ERROR);
        }
    }
}
