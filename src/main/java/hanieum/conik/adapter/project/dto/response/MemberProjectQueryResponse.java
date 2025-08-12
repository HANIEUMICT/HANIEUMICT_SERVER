package hanieum.conik.adapter.project.dto.response;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.entity.ProjectDrawingFile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record MemberProjectQueryResponse(
        @Schema(description = "프로젝트 PK", example = "1")
        Long projectId,

        @Schema(description = "프로젝트 최종 수정일시", example = "2024-01-15T10:30:00")
        LocalDateTime modifiedAt,

        @Schema(description = "프로젝트 상세 내용")
        ProjectRegisterRequest projectRegisterRequest,

        @Schema(description = "프로젝트 도면 파일 목록")
        List<String> drawingUrls
) {
    public static MemberProjectQueryResponse from(Project project){
        List<String> drawingUrls = (project.getDrawingFiles() != null)
                ? project.getDrawingFiles().stream()
                .map(ProjectDrawingFile::getDrawingUrl)
                .toList()
                : List.of();

        return new MemberProjectQueryResponse(
                project.getId(),
                project.getModifiedAt(),
                ProjectRegisterRequest.from(project),
                drawingUrls
        );
    }
}
