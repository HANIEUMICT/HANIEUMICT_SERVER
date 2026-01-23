package hanieum.conik.application.common.mapper;

import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.project.enumerate.ProgressStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ProgressStatusMapper {

    public static List<DealStep> map(ProgressStatus status) {
        return switch (status) {
            case BEFORE -> List.of(DealStep.OPEN, DealStep.REQUESTED);
            case IN_PROGRESS -> List.of(
                    DealStep.CONTRACT_CONFIRMED,
                    DealStep.COMPANY_INSPECTION_COMPLETED,
                    DealStep.SAMPLE_PRODUCTION,
                    DealStep.SAMPLE_PRODUCTION_COMPLETED,
                    DealStep.SAMPLE_DELIVERY,
                    DealStep.SAMPLE_DELIVERED,
                    DealStep.SAMPLE_APPROVED,
                    DealStep.SAMPLE_REJECTED,
                    DealStep.MASS_PRODUCTION,
                    DealStep.MASS_PRODUCTION_COMPLETED,
                    DealStep.PRODUCT_DELIVERY
            );
            case COMPLETED -> List.of(DealStep.CLOSED);
        };
    }

    public static boolean isBefore(ProgressStatus progressStatus) {
        return progressStatus == ProgressStatus.BEFORE;
    }
}
