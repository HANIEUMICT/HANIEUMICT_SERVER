package hanieum.conik.application.common.mapper;

import hanieum.conik.domain.project.enumerate.ProgressStatus;
import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ProgressStatusMapper {

    public static List<ProjectProgressStep> map(ProgressStatus status) {
        return switch (status) {
            case BEFORE -> List.of(ProjectProgressStep.OPEN, ProjectProgressStep.REQUESTED);
            case IN_PROGRESS -> List.of(
                    ProjectProgressStep.CONTRACT_CONFIRMED,
                    ProjectProgressStep.COMPANY_INSPECTION_COMPLETED,
                    ProjectProgressStep.SAMPLE_PRODUCTION,
                    ProjectProgressStep.SAMPLE_PRODUCTION_COMPLETED,
                    ProjectProgressStep.SAMPLE_DELIVERY,
                    ProjectProgressStep.SAMPLE_DELIVERED,
                    ProjectProgressStep.SAMPLE_APPROVED,
                    ProjectProgressStep.SAMPLE_REJECTED,
                    ProjectProgressStep.MASS_PRODUCTION,
                    ProjectProgressStep.MASS_PRODUCTION_COMPLETED,
                    ProjectProgressStep.PRODUCT_DELIVERY
            );
            case COMPLETED -> List.of(ProjectProgressStep.CLOSED);
        };
    }

    public static boolean isBefore(ProgressStatus progressStatus) {
        return progressStatus == ProgressStatus.BEFORE;
    }
}
