package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.PortfolioRequest;
import hanieum.conik.domain.company.dto.PortfolioUpdateRequest;
import hanieum.conik.domain.company.entity.CompanyDetail;

import java.util.List;

public interface PortfolioSaver {
    void add(Long companyId, List<PortfolioRequest> portfolioRequests);

    void delete(Long companyId, List<Long> portfolioIds);

    void sync(CompanyDetail detail, List<PortfolioUpdateRequest> requested);
}
