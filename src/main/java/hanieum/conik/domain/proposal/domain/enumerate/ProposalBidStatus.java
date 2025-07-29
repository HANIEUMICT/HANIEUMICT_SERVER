package hanieum.conik.domain.proposal.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProposalBidStatus {
    PRE_BID("입찰 전"),
    BIDDING("입찰 중"),
    DEAL_REQUESTED("거래 요청받음"),
    ACCEPT_DEAL("거래 수락함"),
    REJECT_DEAL("거래 거절함"),
    BID_SELECTED("낙찰 됨"),
    BID_REJECTED("낙찰 실패");

    private final String description;
}
