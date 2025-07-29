package hanieum.conik.adapter.project.dto.request;

import hanieum.conik.domain.project.enumerate.ProjectBidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BidStatusUpdateRequest (
        @NotNull
        @Schema(description = "bidStatus", example = "BIDDING | BID_CLOSED")
        ProjectBidStatus projectBidStatus,

        @NotNull
        @Schema(description = "publicUntil", example = "2025-12-31")
        LocalDate publicUntil
) { }
