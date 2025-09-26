package hanieum.conik.adapter.favorite.dto;


import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import org.springframework.data.domain.Page;

public record FavoriteResDto(
        ProjectDetailResponse project
) {}
