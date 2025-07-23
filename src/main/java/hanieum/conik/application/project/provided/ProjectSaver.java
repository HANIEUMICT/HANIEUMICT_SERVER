package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.ProjectRegisterRequest;

public interface ProjectSaver {
    Long initiate(Long memberId);

    ProjectRegisterRequest saveProjectDraft(Long memberId, ProjectRegisterRequest projectRegisterRequest);

    Long saveProjectFinal(Long memberId, ProjectRegisterRequest projectRegisterRequest);
}
