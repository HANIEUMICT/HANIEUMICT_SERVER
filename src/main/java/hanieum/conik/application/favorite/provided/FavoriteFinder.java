package hanieum.conik.application.favorite.provided;

import hanieum.conik.domain.favorite.Favorite;
import hanieum.conik.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteFinder {
    Page<Project> findFavoriteProjects(Long companyId, Pageable pageable);
}
