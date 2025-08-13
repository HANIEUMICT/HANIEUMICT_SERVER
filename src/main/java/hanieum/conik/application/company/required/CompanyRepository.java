package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
    Optional<Company> findByIdWithAddresses(@Param("id") Long id);

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.companyDetail WHERE c.id = :id")
    Optional<Company> findByIdWithDetail(@Param("id") Long id);

    @Query("SELECT c from Company c LEFT JOIN FETCH c.companyDetail where c.id in :ids")
    List<Company> findAllWithDetailByIdIn(@Param("ids") Collection<Long> ids);
}
