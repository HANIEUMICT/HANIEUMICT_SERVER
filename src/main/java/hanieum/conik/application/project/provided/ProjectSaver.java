package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;

public interface ProjectSaver {
    Long initiate(Long memberId);

    ProjectRegisterRequest saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    ProjectRegisterRequest saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest);
}
