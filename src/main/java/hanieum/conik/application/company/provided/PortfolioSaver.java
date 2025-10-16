package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.PortfolioRequest;

public interface PortfolioSaver {
    Long add(Long companyDetailId, PortfolioRequest request);

    void delete(Long companyDetailId, Long portfolioId);
}
