package hanieum.conik.application.favorite.required;

import hanieum.conik.domain.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
    boolean existsByCompanyIdAndProjectId(Long companyId, Long projectId);
    long countByProjectId(Long projectId);
}
