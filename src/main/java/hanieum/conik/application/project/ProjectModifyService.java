package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.BidStatusUpdateRequest;
import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectModifyService implements ProjectSaver {
    private final ProjectRepository projectRepository;
    private final ProjectFinder projectFinder;

    @Override
    public Project initiate(Long memberId) {
        try {
            Project project = Project.initiate(memberId);
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectException(ProjectErrorType.PROJECT_INITIATE_ERROR);
        }
    }

    @Override
    public Project saveProjectDraft(Long projectId, ProjectRegisterRequest request) {
        if (!request.submitStatus().equals(SubmitStatus.TEMPORARY_SAVE)) {
            throw new ProjectException(ProjectErrorType.PROJECT_DRAFT_SAVE_ERROR);
        }
        return getSavedProject(projectId, request);
    }

    @Override
    public Project saveProjectFinal(Long projectId, ProjectRegisterRequest request) {
        if (!request.submitStatus().equals(SubmitStatus.SUBMIT)) {
            throw new ProjectException(ProjectErrorType.PROJECT_FINAL_SAVE_ERROR);
        }
        return getSavedProject(projectId, request);
    }

    @Override
    public Project updateProjectBidStatus(Long projectId, BidStatusUpdateRequest bidStatusUpdateRequest) {
        try {
            Project project = projectFinder.findProject(projectId);
            project.updateBidStatusAndPublicUntil(bidStatusUpdateRequest);
            projectRepository.save(project);
            return projectRepository.save(project);
        } catch (Exception e) {
            log.error("에러 발생", e);
            throw new ProjectException(ProjectErrorType.PROJECT_BID_STATUS_UPDATE_ERROR);
        }
    }

    private Project getSavedProject(Long projectId, ProjectRegisterRequest request) {
        try {
            Project project = projectFinder.findProject(projectId);
            project.update(request);

            project.finalizeDrawingFiles();

            projectRepository.save(project);

            return projectRepository.save(project);
        } catch (Exception e) {
            log.error("에러 발생", e);
            throw new ProjectException(ProjectErrorType.PROJECT_SAVE_ERROR);
        }
    }


}
