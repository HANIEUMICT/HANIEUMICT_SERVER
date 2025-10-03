package hanieum.conik.adapter.favorite.webapi;

import hanieum.conik.adapter.favorite.dto.FavoriteResponse;
import hanieum.conik.application.favorite.provided.FavoriteFinder;
import hanieum.conik.application.favorite.provided.FavoriteSaver;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.favorite.dto.FavoriteRequest;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "찜 등록 API", description = """
            ## 특정 프로젝트를 찜합니다.
            - 기업 회원이 특정 프로젝트를 찜합니다.
            - 이미 찜한 프로젝트는 중복으로 찜할 수 없습니다.
            - 찜 등록 성공 시, 찜 ID를 반환합니다.
            """)
    @PostMapping("/{projectId}")
    public ApiResponse<?> registerFavorite(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long projectId) {
        Long companyId = memberFinder.findCompanyIdByMemberId(authDetails.getMemberId());

        favoriteSaver.save(FavoriteRequest.of(companyId, projectId));
        return ApiResponse.success("찜 등록이 완료되었습니다.");
    }

    @Operation(summary = "찜 목록 조회 API", description = """
            ## 기업 회원의 찜 목록을 조회합니다.
            - 기업 회원이 찜한 프로젝트 목록을 조회합니다.
            - 프로젝트의 상세 정보를 포함하여 반환합니다.
            - 페이징 처리를 지원합니다.
            """)
    @GetMapping()
    public ApiResponse<Page<FavoriteResponse>> getFavoriteList(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long companyId = memberFinder.findCompanyIdByMemberId(authDetails.getMemberId());

        Page<FavoriteResponse> favoriteProjects = favoriteFinder.findFavoriteProjects(companyId, pageable);

        return ApiResponse.success(favoriteProjects);
    }

    @Operation(summary = "찜 삭제 API", description = """
            ## 특정 찜을 삭제합니다.
            - 기업 회원이 찜한 프로젝트를 찜 목록에서 삭제합니다.
            - 찜 삭제 성공 시, 삭제된 찜 ID를 반환합니다.
            """)
    @DeleteMapping("/{favoriteId}")
    public ApiResponse<Long> deleteFavorite(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long favoriteId) {
        Long companyId = memberFinder.findCompanyIdByMemberId(authDetails.getMemberId());

        Long deletedId = favoriteSaver.delete(companyId, favoriteId);

        return ApiResponse.success(deletedId);
    }
}
