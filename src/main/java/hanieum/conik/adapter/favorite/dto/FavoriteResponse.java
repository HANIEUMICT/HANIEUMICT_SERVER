package hanieum.conik.adapter.favorite.dto;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.domain.project.entity.Project;

public record FavoriteResponse(
        ProjectDetailResponse project
) {
    public static FavoriteResponse from(Project project) {
        return new FavoriteResponse(
                ProjectDetailResponse.from(project)
        );
    }
}
