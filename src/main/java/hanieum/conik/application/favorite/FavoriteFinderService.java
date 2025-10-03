package hanieum.conik.application.favorite;

import hanieum.conik.adapter.favorite.dto.FavoriteResponse;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.favorite.provided.FavoriteFinder;
import hanieum.conik.application.favorite.required.FavoriteRepository;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.project.entity.Project;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteFinderService implements FavoriteFinder {
    private final FavoriteRepository favoriteRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Page<FavoriteResponse> findFavoriteProjects(Long companyId, Pageable pageable) {
        checkCompanyIsExist(companyId);

        Page<Project> favoriteProjects = favoriteRepository.findAllByCompanyId(companyId, pageable);

        return favoriteProjects.map(FavoriteResponse::from);
    }

    private void checkCompanyIsExist(Long companyId) {
        if(!companyRepository.existsById(companyId)) {
            throw new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND);
        }
    }
}
