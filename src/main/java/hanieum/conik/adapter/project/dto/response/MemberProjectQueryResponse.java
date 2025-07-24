package hanieum.conik.adapter.project.dto.response;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberProjectQueryResponse(
        @Schema(description = "프로젝트 PK", example = "1")
        Long projectId,

        @Schema(description = "프로젝트 상세 내용")
        ProjectRegisterRequest projectRegisterRequest
) {
    public static MemberProjectQueryResponse from(Long projectId, ProjectRegisterRequest projectRegisterRequest) {
        return new MemberProjectQueryResponse(projectId, projectRegisterRequest);
    }
}
