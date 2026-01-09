package hanieum.conik.adapter.project.dto.request;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import org.springframework.data.domain.Page;

public record ProjectListResponse(
        ProjectStatusSummary summary,
        Page<ProjectDetailResponse> projects
) {
    public static ProjectListResponse from(ProjectStatusSummary summary, Page<ProjectDetailResponse> projects) {
        return new ProjectListResponse(summary, projects);
    }
}
