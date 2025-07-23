package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.ProjectRegisterRequest;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.user.exception.UserErrorType;
import hanieum.conik.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hanieum.conik.application.project.required.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectModifyService {
    private final ProjectRepository projectRepository;

    public void register(ProjectRegisterRequest projectRegisterRequest) {
        Project project = projectRepository.findById(projectRegisterRequest.userId())
                .orElseThrow(() -> new UserException(UserErrorType.INVALID_AUTHORIZATION_CODE));
        projectRepository.save(project);
    }
}
