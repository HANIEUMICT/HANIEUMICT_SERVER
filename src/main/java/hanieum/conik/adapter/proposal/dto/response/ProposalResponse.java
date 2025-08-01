package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProposalResponse(
    @Schema(description = "기업 견적서 PK", example = "1")
    Long proposalId,

    @Schema(description = "기업 견적서 최종 수정일시", example = "2024-01-15T10:30:00")
    LocalDateTime modifiedAt,

    @Schema(description = "기업 견적서 상세 내용")
    ProposalRegisterRequest proposalRegisterRequest
) {
    public static ProposalResponse from(Long proposalId, LocalDateTime modifiedAt, ProposalRegisterRequest proposalRegisterRequest) {
        return new ProposalResponse(proposalId, modifiedAt, proposalRegisterRequest);
    }
}

