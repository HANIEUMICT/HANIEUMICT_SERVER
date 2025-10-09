package hanieum.conik.application.project.required;

import hanieum.conik.domain.project.entity.ProjectProgress;
import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long>, JpaSpecificationExecutor<ProjectProgress> {
    @Query("SELECT p.companyId FROM ProjectProgress p WHERE p.projectId = :projectId")
    List<Long> findCompanyIdsByProjectId(Long projectId);

    @Query("SELECT p.companyId FROM ProjectProgress p WHERE p.projectId = :projectId AND p.progressStep IN :steps")
    List<Long> findCompanyIdsByProjectIdAndProgressStepIn(
            Long projectId,
            List<ProjectProgressStep> steps
    );
}
