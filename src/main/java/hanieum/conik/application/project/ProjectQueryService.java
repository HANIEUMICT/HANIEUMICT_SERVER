package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectQueryService implements ProjectFinder {
    private final ProjectRepository projectRepository;

    @Override
    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND));
    }

    @Override
    public List<MemberProjectQueryResponse> getMemberProjects(Long memberId, String status) {
        List<Project> projects;

        if ("finalized".equalsIgnoreCase(status)) {
            projects = projectRepository.findByMemberIdAndIsFinalized(memberId, true);
        } else if ("draft".equalsIgnoreCase(status)) {
            projects = projectRepository.findByMemberIdAndIsFinalized(memberId, false);
        } else {
            projects = projectRepository.findByMemberId(memberId);
        }

        return projects.stream()
                .map(project -> MemberProjectQueryResponse.from(project.getId(),
                        ProjectRegisterRequest.from(project)))
                .collect(Collectors.toList());
    }
}
