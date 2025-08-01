package hanieum.conik.adapter.project.dto.response;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberProjectQueryResponse(
        @Schema(description = "프로젝트 PK", example = "1")
        Long projectId,

        @Schema(description = "프로젝트 상세 내용")
        ProjectRegisterRequest projectRegisterRequest,

        @Schema(description = "프로젝트 도면 파일 목록")
        List<String> drawingUrls
) {
    public static MemberProjectQueryResponse from(Long projectId, ProjectRegisterRequest projectRegisterRequest, List<ProjectDrawingFile> drawingFiles) {
        List<String> drawingUrls = drawingFiles.stream()
                .map(ProjectDrawingFile::getDrawingUrl)
                .toList();
        return new MemberProjectQueryResponse(projectId, projectRegisterRequest, drawingUrls);
    }
}
