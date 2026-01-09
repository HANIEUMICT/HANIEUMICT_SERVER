package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.adapter.project.dto.response.ProjectWithProposalsResponse;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectFinder {
    Project findProject(Long projectId);

    Project validateProjectOpenStatus(Long projectId);

    ProjectWithProposalsResponse getProjectDetailWithProposals(Long projectId);

    Page<ProjectDetailResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus, Pageable pageable);

    Page<ProjectDetailResponse> findProjectsByCompanyId(Long companyId, Pageable pageable);
}
