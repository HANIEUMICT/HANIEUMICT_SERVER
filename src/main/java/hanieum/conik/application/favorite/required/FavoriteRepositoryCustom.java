package hanieum.conik.application.favorite.required;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {
    Page<ProjectDetailResponse> findFavoriteProjects(Long companyId, Pageable pageable);
}
