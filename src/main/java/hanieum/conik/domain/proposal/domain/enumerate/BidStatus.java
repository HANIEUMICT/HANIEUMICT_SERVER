package hanieum.conik.domain.proposal.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidStatus {
    // 프로젝트에선 PRE_BID -> BIDDING -> BID_CLOSED
    // 기업 견적서(입찰)에서는 PRE_BID -> BIDDING -> BID_SELECTED / BID_REJECTED
    PRE_BID("입찰 전"),
    BIDDING("입찰 중"),
    BID_CLOSED("입찰 마감"),
    BID_SELECTED("낙찰 됨"),
    BID_REJECTED("낙찰 실패");

    private final String description;
}
