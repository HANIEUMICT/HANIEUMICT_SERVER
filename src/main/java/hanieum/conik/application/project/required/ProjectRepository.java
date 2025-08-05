package hanieum.conik.application.project.required;

import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMemberId(Long memberId);

    Page<Project> findByMemberIdAndSubmitStatus(Long memberId, SubmitStatus submitStatus, Pageable pageable);

    Page<Project> findByMemberIdAndSubmitStatusIn(Long memberId, List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Project> findBySubmitStatusIn(List<SubmitStatus> submitStatuses, Pageable pageable);

    Page<Project> findBySubmitStatus(SubmitStatus submitStatus, Pageable pageable);
}

