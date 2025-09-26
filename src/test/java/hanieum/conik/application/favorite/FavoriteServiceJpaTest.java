package hanieum.conik.application.favorite;

import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.favorite.required.FavoriteRepository;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.favorite.Favorite;
import hanieum.conik.domain.favorite.dto.FavoriteRequest;
import hanieum.conik.domain.favorite.exception.FavoriteException;
import hanieum.conik.domain.project.ProjectFixtures;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.exception.ProjectException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class FavoriteServiceJpaTest {
    @Autowired FavoriteService favoriteService;
    @Autowired FavoriteRepository favoriteRepository;
    @Autowired ProjectRepository projectRepository;

    @MockBean CompanyRepository companyRepository;

    /***
     * =================================================================================================================
     * save - 관련 테스트
     */

    @Test
    @DisplayName("save: 정상 저장 시 id 반환")
    void save_success() {
        given(companyRepository.existsById(1L)).willReturn(true);
        var p1 = projectRepository.save(ProjectFixtures.minimal("P1"));

        Long id = favoriteService.save(new FavoriteRequest(1L, p1.getId()));

        assertThat(id).isNotNull();
        assertThat(favoriteRepository.existsByCompanyIdAndProjectId(1L, p1.getId())).isTrue();
    }

    @Test
    @DisplayName("save: 회사 없음 → CompanyException")
    void save_companyNotFound() {
        given(companyRepository.existsById(1L)).willReturn(false);

        assertThatThrownBy(() -> favoriteService.save(new FavoriteRequest(1L, 100L)))
                .isInstanceOf(CompanyException.class);
    }

    @Test
    @DisplayName("save: 프로젝트 없음 → ProjectException")
    void save_projectNotFound() {
        given(companyRepository.existsById(1L)).willReturn(true);

        assertThatThrownBy(() -> favoriteService.save(new FavoriteRequest(1L, -1L)))
                .isInstanceOf(ProjectException.class);
    }

    @Test
    @DisplayName("save: 사전 중복 체크 → FavoriteException")
    void save_duplicate_precheck() {
        given(companyRepository.existsById(1L)).willReturn(true);
        var p1 = projectRepository.save(ProjectFixtures.minimal("P1"));

        favoriteRepository.save(Favorite.create(1L, p1.getId()));

        assertThatThrownBy(() -> favoriteService.save(new FavoriteRequest(1L, p1.getId())))
                .isInstanceOf(FavoriteException.class);
    }

    /***
     * =================================================================================================================
     * findFavoriteProjects - 관련 테스트
     */

    @Test
    @DisplayName("findFavoriteProjects: 회사 존재 -> 페이징된 즐겨찾기 반환")
    void findFavoriteProjects_success() {
        given(companyRepository.existsById(1L)).willReturn(true);
        given(companyRepository.existsById(2L)).willReturn(true);

        var p1 = projectRepository.save(ProjectFixtures.minimal("P1"));
        var p2 = projectRepository.save(ProjectFixtures.minimal("P2"));
        var p3 = projectRepository.save(ProjectFixtures.minimal("P3"));

        favoriteService.save(FavoriteRequest.of(1L, p1.getId()));
        favoriteService.save(FavoriteRequest.of(1L, p2.getId()));
        favoriteService.save(FavoriteRequest.of(2L, p3.getId()));

        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        var page = favoriteService.findFavoriteProjects(1L, pageable); // Page<ProjectDetailResponse>

        // then: DTO 기준으로 검증 (엔티티 아님!)
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Project::getId)
                .containsExactlyInAnyOrder(p1.getId(), p2.getId());
    }

    @Test
    @DisplayName("findFavoriteProjects: 회사 없음 -> CompanyException")
    void findFavoriteProjects_companyNotFound() {
        given(companyRepository.existsById(1L)).willReturn(false);

        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        assertThatThrownBy(() -> favoriteService.findFavoriteProjects(1L, pageable))
                .isInstanceOf(CompanyException.class);
    }

    @Test
    @DisplayName("findAllByCompanyId: 회사 존재 but 즐겨찾기 없음 -> 빈 페이지 반환")
    void findFavoriteProjects_noFavorites() {
        given(companyRepository.existsById(1L)).willReturn(true);

        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        var page = favoriteService.findFavoriteProjects(1L, pageable);

        assertThat(page.getTotalElements()).isEqualTo(0L);
        assertThat(page.getContent()).isEmpty();
    }

    /***
     * =================================================================================================================
     * delete - 관련 테스트
     */

    @Test
    @DisplayName("delete: 삭제 성공")
    void delete_success() {
        given(companyRepository.existsById(1L)).willReturn(true);
        var p1 = projectRepository.save(ProjectFixtures.minimal("P1"));

        Long favoriteId = favoriteService.save(new FavoriteRequest(1L, p1.getId()));
        assertThat(favoriteRepository.existsByCompanyIdAndProjectId(1L, p1.getId())).isTrue();

        Optional<Favorite> favorite = favoriteRepository.findById(favoriteId);

        favoriteService.delete(favorite.get().getCompanyId(),favoriteId);

        assertThat(favoriteRepository.existsByCompanyIdAndProjectId(1L, p1.getId())).isFalse();
    }
}
