package hanieum.conik.application.favorite.required;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.domain.favorite.Favorite;
import hanieum.conik.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {
    Page<Project> findFavoriteProjects(Long companyId, Pageable pageable);
}
