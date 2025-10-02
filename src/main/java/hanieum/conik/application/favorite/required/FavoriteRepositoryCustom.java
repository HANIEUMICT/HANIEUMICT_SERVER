package hanieum.conik.application.favorite.required;

import hanieum.conik.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {
    Page<Project> findAllByCompanyId(Long companyId, Pageable pageable);
}
