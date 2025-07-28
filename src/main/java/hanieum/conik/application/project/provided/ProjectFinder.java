package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;

import java.util.List;

public interface ProjectFinder {
    Project findProject(Long projectId);

    List<MemberProjectQueryResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus);
}
