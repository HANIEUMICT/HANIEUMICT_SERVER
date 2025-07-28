package hanieum.conik.adapter.project.dto.request;

import hanieum.conik.domain.proposal.domain.enumerate.BidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BidStatusUpdateRequest (
        @NotNull
        @Schema(description = "bidStatus", example = "BIDDING | BID_CLOSED")
        BidStatus bidStatus,

        @NotNull
        @Schema(description = "publicUntil", example = "2025-12-31")
        LocalDate publicUntil
) { }
