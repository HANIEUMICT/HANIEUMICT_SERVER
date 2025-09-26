package hanieum.conik.application.favorite.required;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.domain.favorite.QFavorite;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.entity.QProject;
import hanieum.conik.domain.project.entity.QProjectDrawingFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{
    private final JPAQueryFactory query;
    private static final QFavorite f = QFavorite.favorite;
    private static final QProject p = QProject.project;
    private static final QProjectDrawingFile df = QProjectDrawingFile.projectDrawingFile; // 컬렉션


    @Override
    public Page<ProjectDetailResponse> findFavoriteProjects(Long companyId, Pageable pageable) {
        Pageable safe = pageable == null ? Pageable.unpaged() : pageable;

        // 1) Favorite 기준으로 페이지 자르기 (기본: createdAt DESC)
        OrderSpecifier<?> order = toFavoriteOrder(safe.getSort());
        List<Long> pageProjectIds = query
                .select(f.projectId)
                .from(f)
                .where(f.companyId.eq(companyId), f.isDeleted.isFalse())
                .orderBy(order)
                .offset(safe.isPaged() ? safe.getOffset() : 0)
                .limit(safe.isPaged() ? safe.getPageSize() : Long.MAX_VALUE)
                .fetch();

        long total = Optional.ofNullable(
                query.select(f.count())
                        .from(f)
                        .where(f.companyId.eq(companyId), f.isDeleted.isFalse())
                        .fetchOne()
        ).orElse(0L);

        if (pageProjectIds.isEmpty()) {
            return new PageImpl<>(List.of(), safe, total);
        }

        // 2) 선택된 page 범위의 Project들 로드 (도면파일 fetch join, 중복 제거)
        List<Project> projects = query
                .selectFrom(p).distinct()
                .leftJoin(p.drawingFiles, df).fetchJoin()
                .where(p.id.in(pageProjectIds), p.isDeleted.isFalse())
                .fetch();

        // 3) DTO 매핑 + Favorite 페이지 순서 유지
        Map<Long, ProjectDetailResponse> byId = projects.stream()
                .collect(Collectors.toMap(Project::getId, ProjectDetailResponse::from));

        List<ProjectDetailResponse> ordered = pageProjectIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .toList();

        return new PageImpl<>(ordered, safe, total);
    }

    private OrderSpecifier<?> toFavoriteOrder(Sort sort) {
        if (sort == null || sort.isEmpty()) return f.createdAt.desc();
        // 필요한 필드만 매핑 허용
        Sort.Order o = sort.getOrderFor("createdAt");
        if (o != null) return o.isAscending() ? f.createdAt.asc() : f.createdAt.desc();

        o = sort.getOrderFor("projectId");
        if (o != null) return o.isAscending() ? f.projectId.asc() : f.projectId.desc();

        return f.createdAt.desc();
    }
}
