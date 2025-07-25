package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;

public interface ProjectSaver {
    Long initiate(Long memberId);

    MemberProjectQueryResponse saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    MemberProjectQueryResponse saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest);
}
