package hanieum.conik.application.favorite.required;

import hanieum.conik.domain.favorite.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
    Page<Favorite> findAllByCompanyId(Long companyId, Pageable pageable);
    boolean existsByCompanyIdAndProjectId(Long companyId, Long projectId);
}
