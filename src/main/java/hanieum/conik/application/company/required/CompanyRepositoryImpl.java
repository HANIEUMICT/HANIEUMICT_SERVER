package hanieum.conik.application.company.required;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanieum.conik.adapter.company.response.CompanyProfileResponse;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.QCompany;
import hanieum.conik.domain.company.entity.QCompanyDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom{
    private final JPAQueryFactory qf;

    @Override
    public Page<CompanyProfileResponse> findCompaniesWithFilter(CompanyProfileSearchCondition cond, Pageable pageable) {
        QCompany c = QCompany.company;
        QCompanyDetail d = QCompanyDetail.companyDetail;

        BooleanBuilder where = new BooleanBuilder();

        // 1. 카테고리(업종) 리스트 필터링
        if (cond.categories() != null && !cond.categories().isEmpty()) {
            // 여러 카테고리 중 하나라도 포함되면 검색 (OR 조건)
            where.and(c.industry.in(cond.categories()));
        }

        // 2. 평점 필터링 (평점은 평균 점수이므로 데이터가 없는 신규 업체는 null임)
        if (cond.minRating() != null) {
            // 평점이 null(데이터 없음)이거나, 입력한 기준치 이상인 경우 포함
            where.and(d.rating.isNull().or(d.rating.goe(cond.minRating())));
        }

        // 3. 응답 시간 필터링 (이하)
        if (cond.maxResponseMinutes() != null) {
            where.and(d.avgResponseMinutes.loe(cond.maxResponseMinutes()));
        }

        // 4. 거래 건수 필터링 (이상)
        if (cond.minTotalOrderCount() != null) {
            where.and(d.totalOrderCount.goe(cond.minTotalOrderCount()));
        }

        // 5. 제작 기간 필터링 (이하)
        if (cond.maxProductionHours() != null) {
            where.and(d.avgProductionLeadHours.loe(cond.maxProductionHours()));
        }

        OrderSpecifier<?>[] orderSpecs = toOrderSpec(pageable.getSort(), c, d);

        // 데이터 조회 쿼리
        List<CompanyProfileResponse> content = qf
                .select(Projections.constructor(CompanyProfileResponse.class,
                        c.id,
                        c.name,
                        d.logoUrl,
                        d.avgProductionLeadHours,
                        d.totalOrderCount,
                        d.repeatOrderCount,
                        d.avgResponseMinutes))
                .from(c)
                .join(c.companyDetail, d)
                .where(where)
                .orderBy(orderSpecs)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 (최적화)
        JPAQuery<Long> countQuery = qf
                .select(c.count())
                .from(c)
                .join(c.companyDetail, d)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] toOrderSpec(Sort sort, QCompany c, QCompanyDetail d) {
        if (sort == null || sort.isUnsorted()) {
            return new OrderSpecifier[]{
                    // 평균 점수(rating)가 높은 순을 기본으로 하되, 점수 없는 업체는 뒤로
                    d.rating.desc().nullsLast(),
                    d.totalOrderCount.desc().nullsLast(),
                    c.id.desc()
            };
        }

        List<OrderSpecifier<?>> list = new ArrayList<>();
        for (Sort.Order o : sort) {
            ComparableExpressionBase<?> path = switch (o.getProperty()) {
                case "name"                   -> c.name;
                case "rating"                 -> d.rating;
                case "avgResponseMinutes"     -> d.avgResponseMinutes;
                case "totalOrderCount"        -> d.totalOrderCount;
                case "avgProductionLeadHours" -> d.avgProductionLeadHours;
                default -> c.id;
            };
            list.add(o.isAscending() ? path.asc() : path.desc());
        }
        return list.toArray(OrderSpecifier[]::new);
    }

    @Override
    public Page<Company> search(CompanySummarySearchCondition cond, Pageable pageable) {
        QCompany c = QCompany.company;
        QCompanyDetail d = QCompanyDetail.companyDetail;

        BooleanBuilder where = new BooleanBuilder();

        if (cond != null) {
            if (org.springframework.util.StringUtils.hasText(cond.name())) {
                where.and(c.name.containsIgnoreCase(cond.name().trim()));
            }
            if (org.springframework.util.StringUtils.hasText(cond.region())) {
                String kw = cond.region().trim();
                where.and(
                        c.address.streetAddress.containsIgnoreCase(kw)
                                .or(c.address.detailAddress.containsIgnoreCase(kw))
                );
            }
            if (org.springframework.util.StringUtils.hasText(cond.businessType())) {
                where.and(c.businessType.containsIgnoreCase(cond.businessType().trim()));
            }
        }

        JPAQuery<Company> contentQuery = qf
                .selectFrom(c)
                .leftJoin(c.companyDetail, d).fetchJoin()
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (pageable.getSort().isEmpty()) {
            contentQuery.orderBy(c.name.asc());
        } else {
            for (Sort.Order order : pageable.getSort()) {
                String prop = order.getProperty();
                boolean asc = order.isAscending();

                if ("addressName".equalsIgnoreCase(prop)) {
                    contentQuery.orderBy(asc ? c.name.asc() : c.name.desc());
                } else if ("createdAt".equalsIgnoreCase(prop)) {
                    contentQuery.orderBy(asc ? c.createdAt.asc() : c.createdAt.desc());
                } else if ("businessType".equalsIgnoreCase(prop)) {
                    contentQuery.orderBy(asc ? c.businessType.asc() : c.businessType.desc());
                }
            }
        }

        List<Company> content = contentQuery.fetch();

        JPAQuery<Long> countQuery = qf
                .select(c.count())
                .from(c)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
