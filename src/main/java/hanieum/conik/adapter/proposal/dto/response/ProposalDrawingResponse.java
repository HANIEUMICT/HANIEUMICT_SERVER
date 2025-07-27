package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.domain.proposal.domain.entity.ProposalDrawingFile;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProposalDrawingResponse(
        @Schema(description = "도면 파일 ID", example = "1")
        Long drawingFileId,

        @Schema(description = "도면 파일 URL")
        String drawingUrl
) {
    public static ProposalDrawingResponse from(ProposalDrawingFile drawingFile) {
        return new ProposalDrawingResponse(
                drawingFile.getId(),
                drawingFile.getDrawingUrl()
        );
    }
}