package hanieum.conik.domain.favorite;

import hanieum.conik.QueryDslTestConfig;
import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.application.favorite.required.FavoriteRepository;
import hanieum.conik.domain.project.entity.Project;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@Transactional
class FavoriteRepositoryTest {
    @Autowired FavoriteRepository favoriteRepository;

    @Test
    @DisplayName("즐겨찾기 저장")
    void 즐겨찾기_저장() {
        // given
        Favorite favorite = Favorite.create(1L, 100L);

        // when
        Favorite savedFavorite = favoriteRepository.save(favorite);

        // then
        assertThat(savedFavorite.getId()).isNotNull();
        assertThat(savedFavorite.getCompanyId()).isEqualTo(1L);
        assertThat(savedFavorite.getProjectId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("company id로 즐겨찾기 조회")
    void company_id로_즐겨찾기_조회() {
        // given
        Favorite favorite1 = Favorite.create(1L, 100L);
        Favorite favorite2 = Favorite.create(1L, 101L);
        Favorite favorite3 = Favorite.create(2L, 200L);
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);
        favoriteRepository.save(favorite3);

        // when
        var favorites = favoriteRepository.findAll().stream()
                .filter(fav -> fav.getCompanyId().equals(1L))
                .toList();

        // then
        assertThat(favorites).hasSize(2);
        assertThat(favorites).extracting("projectId").containsExactlyInAnyOrder(100L, 101L);
    }

    @Test
    @DisplayName("companyId로 페이징 조회")
    void findAllByCompanyId_paging() {
        // given
        favoriteRepository.save(Favorite.create(1L, 100L));
        favoriteRepository.save(Favorite.create(1L, 101L));
        favoriteRepository.save(Favorite.create(2L, 200L));

        // when
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        var page = favoriteRepository.findFavoriteProjects(1L, pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(ProjectDetailResponse::projectId)
                .containsExactlyInAnyOrder(100L, 101L);
    }

    @Test
    @DisplayName("companyId로 조회시 없는 경우 빈 페이지 반환")
    void findAllByCompanyId_empty() {
        // given
        favoriteRepository.save(Favorite.create(2L, 200L));

        // when
        var page = favoriteRepository.findAllByCompanyId(1L, PageRequest.of(0, 10));

        // then
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}
