package hanieum.conik.application.favorite;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.application.favorite.provided.FavoriteFinder;
import hanieum.conik.application.favorite.provided.FavoriteSaver;
import hanieum.conik.application.favorite.required.FavoriteRepository;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.favorite.Favorite;
import hanieum.conik.domain.favorite.dto.FavoriteRequest;
import hanieum.conik.domain.favorite.exception.FavoriteErrorType;
import hanieum.conik.domain.favorite.exception.FavoriteException;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService implements FavoriteSaver, FavoriteFinder {
    private final FavoriteRepository favoriteRepository;
    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Page<ProjectDetailResponse> findFavoriteProjects(Long companyId, Pageable pageable) {
        checkCompanyIsExist(companyId);

        Page<ProjectDetailResponse> favoriteProjects = favoriteRepository.findFavoriteProjects(companyId, pageable);

        if (favoriteProjects == null) return Page.empty(pageable);

        return favoriteProjects;
    }

    @Override
    public Long save(FavoriteRequest request) {
        checkCompanyIsExist(request.companyId());

        checkProjectIsExist(request.projectId());

        checkDuplicated(request);

        Favorite favorite = Favorite.create(request.companyId(), request.projectId());
        Favorite saved = favoriteRepository.save(favorite);

        return saved.getId();
    }

    @Override
    public Long delete(Long companyId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteException(FavoriteErrorType.FAVORITE_NOT_FOUND));

        if(!favorite.getCompanyId().equals(companyId)) {
            throw new FavoriteException(FavoriteErrorType.UNAUTHORIZED);
        }

        favoriteRepository.delete(favorite);
        return favorite.getId();
    }

    private void checkDuplicated(FavoriteRequest request) {
        if(favoriteRepository.existsByCompanyIdAndProjectId(request.companyId(), request.projectId())){
            throw new FavoriteException(FavoriteErrorType.DUPLICATE_FAVORITE);
        }
    }

    private void checkProjectIsExist(Long projectId) {
        if(!projectRepository.existsById(projectId)) {
            throw new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND);
        }
    }

    private void checkCompanyIsExist(Long companyId) {
        if(!companyRepository.existsById(companyId)) {
            throw new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND);
        }
    }
}
