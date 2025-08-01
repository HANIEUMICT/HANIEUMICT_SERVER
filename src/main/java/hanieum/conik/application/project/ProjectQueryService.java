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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<MemberProjectQueryResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus) {
        List<Project> projects = (submitStatus == null)
                ? projectRepository.findByMemberId(memberId)
                : projectRepository.findByMemberIdAndSubmitStatus(memberId, submitStatus);

        return projects.stream()
                .map(project -> MemberProjectQueryResponse.from(project.getId(), ProjectRegisterRequest.from(project)))
                .collect(Collectors.toList());
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
