package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    @Query("select distinct p from Proposal p " +
            "left join fetch p.items " +
            "left join fetch p.drawingFiles " +
            "where p.id = :id")
    Optional<Proposal> findDetailById(@Param("id") Long id);


    List<Proposal> findByProjectIdAndSubmitStatus(Long projectId, SubmitStatus submitStatus);

    Page<Proposal> findByCompanyIdAndSubmitStatusIn(Long companyId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Proposal> findByCompanyIdAndSubmitStatus(Long companyId, SubmitStatus submitStatus, Pageable pageable);

    Page<Proposal> findByCompanyIdAndProjectIdAndSubmitStatusIn(Long companyId, Long projectId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Proposal> findByCompanyIdAndProjectIdAndSubmitStatus(Long companyId, Long projectId, SubmitStatus submitStatus, Pageable pageable);

    Page<Proposal> findByCompanyId(Long companyId, Pageable pageable);
}
