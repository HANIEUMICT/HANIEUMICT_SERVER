package hanieum.conik.domain.proposal.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidStatus {
    PRE_BID("입찰 전"),
    BIDDING("입찰 중"),
    BID_SELECTED("낙찰 됨"),
    BID_REJECTED("낙찰 실패");

    private final String description;
}
