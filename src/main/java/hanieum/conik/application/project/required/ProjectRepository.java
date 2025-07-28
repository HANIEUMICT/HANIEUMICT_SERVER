package hanieum.conik.application.project.required;

import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMemberId(Long memberId);

    List<Project> findByMemberIdAndSubmitStatus(Long memberId, SubmitStatus submitStatus);
}

