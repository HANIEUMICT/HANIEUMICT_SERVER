package hanieum.conik.application.deal.provided;

import hanieum.conik.adapter.deal.dto.response.DealSummaryResponse;
import hanieum.conik.adapter.deal.dto.response.DealTimelineResponse;
import hanieum.conik.global.adapter.security.AuthDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DealFinder {

    Page<DealSummaryResponse> getDeals(AuthDetails authDetails, Pageable pageable);

    DealTimelineResponse getDealTimeline(AuthDetails authDetails, Long dealId);
}
