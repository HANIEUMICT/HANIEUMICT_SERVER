package hanieum.conik.application.deal.required;

import feign.Param;
import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.enumerate.DealStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {
    @Query("SELECT p.companyId FROM Deal p WHERE p.projectId = :projectId")
    List<Long> findCompanyIdsByProjectId(Long projectId);

    @Query("SELECT p.companyId FROM Deal p WHERE p.projectId = :projectId AND p.dealStep IN :steps")
    List<Long> findCompanyIdsByProjectIdAndProgressStepIn(
            Long projectId,
            List<DealStep> steps
    );

    @Query("SELECT d FROM Deal d WHERE d.dealStep in :steps")
    Page<Deal> findAcceptedDeals(@Param("steps") List<DealStep> steps, Pageable pageable);

    Optional<Deal> findByProjectId(Long projectId);
}
