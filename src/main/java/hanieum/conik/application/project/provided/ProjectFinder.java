package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectFinder {
    Project findProject(Long projectId);

    Project validateProjectOpenStatus(Long projectId);

    MemberProjectQueryResponse queryProjectDetail(Long projectId, Long memberId);

    Page<MemberProjectQueryResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus, Pageable pageable);
}
