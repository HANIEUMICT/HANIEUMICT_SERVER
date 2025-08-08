package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
    Optional<Company> findByIdWithAddresses(@Param("id") Long id);
}
