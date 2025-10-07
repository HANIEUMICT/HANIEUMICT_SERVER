package hanieum.conik.adapter.project.dto.request;

public record ProjectStatusSummary(
        long beforeCount,
        long inProgressCount,
        long completedCount,
        long totalCount
) {
    public static ProjectStatusSummary from(long before, long inProgress, long completed) {
        return new ProjectStatusSummary(
                before,
                inProgress,
                completed,
                before + inProgress + completed
        );
    }
}
