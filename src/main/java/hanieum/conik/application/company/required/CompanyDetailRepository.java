package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.CompanyDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, Long> {
    @EntityGraph(attributePaths = {"equipments", "portfolios"})
    Optional<CompanyDetail> findById(Long id);
}