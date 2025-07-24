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
import java.util.Map;
import java.util.function.Supplier;
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
        Map<String, Supplier<List<Project>>> strategies = Map.of(
                "finalized", () -> projectRepository.findByMemberIdAndIsFinalized(memberId, true),
                "draft", () -> projectRepository.findByMemberIdAndIsFinalized(memberId, false)
        );

        List<Project> projects = strategies.getOrDefault(status != null ? status.toLowerCase() : "all",
                () -> projectRepository.findByMemberId(memberId)).get();

        return projects.stream()
                .map(project -> MemberProjectQueryResponse.from(project.getId(),
                        ProjectRegisterRequest.from(project)))
                .collect(Collectors.toList());
    }
}
