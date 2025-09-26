package hanieum.conik.adapter.favorite.webapi;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.favorite.provided.FavoriteFinder;
import hanieum.conik.application.favorite.provided.FavoriteSaver;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.favorite.dto.FavoriteRequest;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/company/favorites")
@Tag(name = "Favorite", description = "찜 API")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteSaver favoriteSaver;
    private final FavoriteFinder favoriteFinder;
    private final MemberFinder memberFinder;

    @PostMapping("/{projectId}")
    public ApiResponse<Long> registerFavorite(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long projectId) {
        Long companyId = memberFinder.findById(authDetails.getMemberId()).getCompanyId();

        Long saved = favoriteSaver.save(FavoriteRequest.of(companyId, projectId));
        return ApiResponse.success(saved);
    }

    @GetMapping()
    public ApiResponse<Page<ProjectDetailResponse>> getFavoriteList(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long companyId = memberFinder.findById(authDetails.getMemberId()).getCompanyId();

        return ApiResponse.success(favoriteFinder.findFavoriteProjects(companyId, pageable));
    }

    @DeleteMapping("/{favoriteId}")
    public ApiResponse<Long> deleteFavorite(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long favoriteId) {
        Long companyId = memberFinder.findById(authDetails.getMemberId()).getCompanyId();

        Long deletedId = favoriteSaver.delete(companyId, favoriteId);

        return ApiResponse.success(deletedId);
    }
}
