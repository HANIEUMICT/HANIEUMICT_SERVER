package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.common.email.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom{
    Optional<Company> findByEmail(Email email);

    Page<Company> findAll(Pageable pageable);

    @Query("""
            select c
            from Company c
            join fetch c.companyDetail
            where c.id = :id
            """)
    Optional<Company> findWithDetailById(Long id);

    @Query("select c from Company c left join fetch c.companyDetail")
    List<Company> findAllWithDetail();
}
