package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberProposalResponse (
    @Schema(description = "기업 견적서 PK", example = "1")
    Long proposalId,

    @Schema(description = "기업 견적서 상세 내용")
    ProposalRegisterRequest proposalRegisterRequest
) {
    public static MemberProposalResponse from(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        return new MemberProposalResponse(proposalId, proposalRegisterRequest);
    }
}

