package hanieum.conik.application.project.required;

import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    List<Project> findByMemberId(Long memberId);

    @Query("""
        select distinct p
        from Project p
        left join fetch p.drawingFiles df
        where p.id = :id
    """)
    Optional<Project> findByIdWithDrawingFiles(@Param("id") Long id);

    Page<Project> findByMemberIdAndSubmitStatus(Long memberId, SubmitStatus submitStatus, Pageable pageable);

    Page<Project> findByMemberIdAndSubmitStatusIn(Long memberId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Project> findBySubmitStatusIn(List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Project> findBySubmitStatus(SubmitStatus submitStatus, Pageable pageable);

    @Query("""
        select distinct p
        from Project p, DealRequest pr
        where pr.projectId = p.id
          and pr.companyId = :companyId
    """)
    Page<Project> findByCompanyId(@Param("companyId")Long companyId, Pageable pageable);

    List<Project> findByIdIn(Collection<Long> ids);
}

