package hanieum.conik.application.favorite.provided;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.domain.favorite.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteFinder {
    Page<ProjectDetailResponse> findFavoriteProjects(Long companyId, Pageable pageable);
}
