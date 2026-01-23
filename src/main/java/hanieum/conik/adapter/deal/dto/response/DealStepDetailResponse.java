package hanieum.conik.adapter.deal.dto.response;

import hanieum.conik.domain.deal.enumerate.DealStep;

import java.util.Map;

public record DealStepDetailResponse(
        DealStep step,
        String title,
        Map<String, Object> detail
) {
    public static DealStepDetailResponse empty(DealStep step) {
        return new DealStepDetailResponse(
                step,
                step.getDescription(),
                null
        );
    }

    public static DealStepDetailResponse from(
            DealStep step,
            Map<String, Object> detail
    ) {
        return new DealStepDetailResponse(
                step,
                step.getDescription(),
                detail
        );
    }
}
