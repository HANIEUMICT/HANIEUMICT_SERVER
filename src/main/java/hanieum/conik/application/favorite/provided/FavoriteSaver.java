package hanieum.conik.application.favorite.provided;

import hanieum.conik.domain.favorite.dto.FavoriteRequest;

public interface FavoriteSaver {
    Long save(FavoriteRequest request);

    Long delete(Long companyId, Long favoriteId);
}
