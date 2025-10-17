package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.PortfolioSaver;
import hanieum.conik.application.company.required.PortfolioRepository;
import hanieum.conik.domain.company.dto.PortfolioRequest;
import hanieum.conik.domain.company.dto.PortfolioUpdateRequest;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyDetail;
import hanieum.conik.domain.company.entity.Portfolio;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioModifyService implements PortfolioSaver {
    private final CompanyFinder companyFinder;
    private final PortfolioRepository portfolioRepository;

    @Override
    public void add(Long companyId, List<PortfolioRequest> portfolioRequests) {
        Company company = companyFinder.findCompany(companyId);

        CompanyDetail detail = company.getCompanyDetail();
        if (detail == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_NOT_FOUND);
        }

        if(portfolioRequests == null || portfolioRequests.isEmpty()) {
            return;
        }

        List<Portfolio> list = portfolioRequests.stream()
                .map(Portfolio::create)
                .toList();

        list.forEach(detail::addPortfolio);

        portfolioRepository.saveAllAndFlush(list);
    }

    @Override
    public void delete(Long companyId, List<Long> portfolioIds) {
        Company company = companyFinder.findCompany(companyId);
        CompanyDetail detail = company.getCompanyDetail();
        if (detail == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_NOT_FOUND);
        }

        List<Long> distinctIds = (portfolioIds == null) ? List.of()
                : portfolioIds.stream().filter(java.util.Objects::nonNull).distinct().toList();
        long deletedCount = portfolioRepository.deleteByCompanyDetailIdAndIdIn(detail.getId(), distinctIds);
        if (deletedCount != distinctIds.size()) {
            throw new CompanyException(CompanyErrorType.PORTFOLIO_NOT_FOUND);
        }
    }

    @Override
    public void sync(CompanyDetail detail, List<PortfolioUpdateRequest> requested) {
        updateAddKeepDelete(
                detail.getPortfolios(),
                requested,
                Portfolio::getId,
                PortfolioUpdateRequest::id,
                this::newPortfolioFrom,
                detail::addPortfolio,
                detail::removePortfolio
        );
    }

    private Portfolio newPortfolioFrom(PortfolioUpdateRequest request) {
        return Portfolio.create(PortfolioRequest.fromPortfolioUpdateRequest(request));
    }

    /** EquipmentModifyService와 동일한 제네릭 델타 유틸 */
    private <E, U> void updateAddKeepDelete(
            List<E> current,
            List<U> requested,
            Function<E, Long> currentIdFn,
            Function<U, Long> requestedIdFn,
            Function<U, E> creator,
            Consumer<E> adder,
            Consumer<E> remover
    ) {
        if (requested == null) requested = List.of();

        // (1) 요청 내 중복 ID 방지
        Set<Long> seen = new HashSet<>();
        for (U u : requested) {
            Long rid = requestedIdFn.apply(u);
            if (rid != null && !seen.add(rid)) {
                throw new GlobalException(GlobalErrorType.DUPLICATED_CHILD_ID);
            }
        }

        // (2) 현재(DB) 엔티티를 ID 맵으로
        Map<Long, E> byId = current.stream()
                .filter(e -> currentIdFn.apply(e) != null)
                .collect(Collectors.toMap(currentIdFn, Function.identity()));

        // (3) 유지 대상 ID 집합
        Set<Long> keep = new HashSet<>();

        // (4) 요청 스냅샷 순회 → 신규 추가 or 유지
        for (U u : requested) {
            Long rid = requestedIdFn.apply(u);
            if (rid == null) {
                E created = creator.apply(u);
                adder.accept(created);
            } else {
                E exist = byId.get(rid);
                if (exist == null) {
                    // 포트폴리오 전용 예외 사용
                    throw new CompanyException(CompanyErrorType.PORTFOLIO_NOT_FOUND);
                }
                keep.add(rid);
            }
        }

        // (5) 삭제: 현재(DB)에 있으나 keep에 없는 것들
        List<E> toRemove = current.stream()
                .filter(e -> {
                    Long id = currentIdFn.apply(e);
                    return id != null && !keep.contains(id);
                })
                .toList();

        toRemove.forEach(remover);
    }
}
