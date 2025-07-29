package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectBidStatus {
    PRE_BID("입찰 전"),
    BIDDING("입찰 중"),
    BID_CLOSED("입찰 마감");

    private final String description;
}

