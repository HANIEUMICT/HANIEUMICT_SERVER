package hanieum.conik.domain.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FavoriteRequest (
        @Schema(description = "회사 ID", example = "1")
        @NotNull(message = "회사 ID는 필수 입력입니다.")
        Long companyId,

        @Schema(description = "프로젝트 ID", example = "1")
        @NotNull(message = "프로젝트 ID는 필수 입력입니다.")
        Long projectId
) {
    public static FavoriteRequest of(Long companyId, Long projectId) {
        return new FavoriteRequest(companyId, projectId);
    }
}