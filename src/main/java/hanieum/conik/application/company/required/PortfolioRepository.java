package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByIdAndCompanyDetailId(Long portfolioId, Long companyDetailId);
    List<Portfolio> findByCompanyDetailId(Long companyId);
    long deleteByCompanyDetailIdAndIdIn(Long companyDetailId, List<Long> portfolioId);
}
