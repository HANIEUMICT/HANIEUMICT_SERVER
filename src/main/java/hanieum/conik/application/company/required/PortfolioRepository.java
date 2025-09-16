package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByCompanyDetailId(Long companyId);
}
