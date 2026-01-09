package hanieum.conik.application.project.required;

import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.enumerate.DealStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectProgressRepository extends JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {
    @Query("SELECT p.companyId FROM Deal p WHERE p.projectId = :projectId")
    List<Long> findCompanyIdsByProjectId(Long projectId);

    @Query("SELECT p.companyId FROM Deal p WHERE p.projectId = :projectId AND p.progressStep IN :steps")
    List<Long> findCompanyIdsByProjectIdAndProgressStepIn(
            Long projectId,
            List<DealStep> steps
    );
}
