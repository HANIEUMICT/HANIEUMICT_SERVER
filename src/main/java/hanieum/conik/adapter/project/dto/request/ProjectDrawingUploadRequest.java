package hanieum.conik.adapter.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ProjectDrawingUploadRequest (
        @NotBlank
        @Schema(description = "프로젝트 아이디", example = "1")
        Long projectId,

        @NotBlank
        @Schema(description = "Object url", example = "https://conik-bucket.s3.ap-northeast-2.amazonaws.com/prefix/filename.png")
        String drawingUrl
){ }
