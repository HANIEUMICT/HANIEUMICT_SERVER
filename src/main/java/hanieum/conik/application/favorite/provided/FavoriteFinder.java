package hanieum.conik.application.favorite.provided;

import hanieum.conik.adapter.favorite.dto.FavoriteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteFinder {
    Page<FavoriteResponse> findFavoriteProjects(Long companyId, Pageable pageable);
}
