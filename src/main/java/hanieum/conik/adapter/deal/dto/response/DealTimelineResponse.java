package hanieum.conik.adapter.deal.dto.response;

import hanieum.conik.domain.deal.enumerate.DealStep;

import java.util.List;

public record DealTimelineResponse(
        Long dealId,
        DealStep currentStep,
        List<DealStepDetailResponse> timeline
) {
}
