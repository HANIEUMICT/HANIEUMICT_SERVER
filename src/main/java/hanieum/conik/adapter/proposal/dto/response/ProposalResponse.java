package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProposalResponse(
    @Schema(description = "기업 견적서 PK", example = "1")
    Long proposalId,

    @Schema(description = "기업 견적서 상세 내용")
    ProposalRegisterRequest proposalRegisterRequest
) {
    public static ProposalResponse from(Long proposalId, ProposalRegisterRequest proposalRegisterRequest) {
        return new ProposalResponse(proposalId, proposalRegisterRequest);
    }
}

