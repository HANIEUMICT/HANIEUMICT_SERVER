package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectQueryService implements ProjectFinder {
    private final ProjectRepository projectRepository;

    @Override
    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND));
    }

    @Override
    public Page<MemberProjectQueryResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus, Pageable pageable) {
        Page<Project> projects = findProjectsWithStatus(submitStatus, memberId, pageable);

        return projects.map(project -> MemberProjectQueryResponse.from(
                        project.getId(),
                        ProjectRegisterRequest.from(project),
                        project.getDrawingFiles()
                ));
    }

    private Page<Project> findProjectsWithStatus(SubmitStatus submitStatus, Long memberId, Pageable pageable) {
        return (submitStatus == null)
                ? projectRepository.findByMemberIdAndSubmitStatusIn(memberId, List.of(SubmitStatus.TEMPORARY_SAVE, SubmitStatus.SUBMIT), pageable)
                : projectRepository.findByMemberIdAndSubmitStatus(memberId, submitStatus, pageable);
    }

    @Override
    public Project validateProjectOpenStatus(Long projectId){
        Project project = findProject(projectId);
        if (project.getPublicUntil().isBefore(LocalDate.now())) {
            throw new ProjectException(ProjectErrorType.PROJECT_EXPIRED);
        }
        return project;
    }
}
