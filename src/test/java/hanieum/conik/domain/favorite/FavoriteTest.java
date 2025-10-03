package hanieum.conik.domain.favorite;

import hanieum.conik.domain.favorite.dto.FavoriteRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    @Test
    void create_shouldSetCompanyIdAndProjectId() {
        // when
        Favorite fav = Favorite.create(1L, 100L);

        // then
        assertThat(fav.getId()).isNull();
        assertThat(fav.getCompanyId()).isEqualTo(1L);
        assertThat(fav.getProjectId()).isEqualTo(100L);
    }
}