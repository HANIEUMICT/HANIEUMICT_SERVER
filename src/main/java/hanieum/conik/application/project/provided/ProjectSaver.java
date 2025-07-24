package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;

public interface ProjectSaver {
    Long initiate(Long memberId);

    ProjectRegisterRequest saveProjectDraft(Long memberId, ProjectRegisterRequest projectRegisterRequest);

    ProjectRegisterRequest saveProjectFinal(Long memberId, ProjectRegisterRequest projectRegisterRequest);
}
