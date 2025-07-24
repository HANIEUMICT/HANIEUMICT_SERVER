package hanieum.conik.adapter.proposal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProposalDrawingUploadRequest(
        @NotNull
        @Schema(description = "공장 견적서 아이디", example = "1")
        Long proposalId,

        @NotBlank
        @Schema(description = "Object url", example = "https://conik-bucket.s3.ap-northeast-2.amazonaws.com/prefix/filename.png")
        String drawingUrl
){ }
