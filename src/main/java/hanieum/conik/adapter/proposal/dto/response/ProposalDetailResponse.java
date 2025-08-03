package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ProposalDetailResponse(
        @Schema(description = "기업 견적서 PK", example = "1")
        Long proposalId,

        @Schema(description = "프로젝트 ID", example = "1")
        Long projectId,

        @Schema(description = "회사 ID", example = "1")
        Long companyId,

        @Schema(description = "견적 총액", example = "1000000")
        Long totalPrice,

        @Schema(description = "1차 지급 금액", example = "800000")
        Long firstPrice,

        @Schema(description = "2차 지급 금액", example = "200000")
        Long secondPrice,

        @Schema(description = "견적서 비고", example = "추가 할인 가능")
        String proposalNote,

        @Schema(description = "작성 상태")
        SubmitStatus submitStatus,

        @Schema(description = "견적 항목 리스트")
        List<ProposalItemResponse> items,

        @Schema(description = "도면 파일 리스트")
        List<ProposalDrawingResponse> drawingFiles
) {
    public static ProposalDetailResponse from(Proposal proposal) {
        return new ProposalDetailResponse(
                proposal.getId(),
                proposal.getProjectId(),
                proposal.getCompanyId(),
                proposal.getTotalPrice(),
                proposal.getFirstPrice(),
                proposal.getSecondPrice(),
                proposal.getProposalNote(),
                proposal.getSubmitStatus(),
                proposal.getItems().stream()
                        .map(ProposalItemResponse::from)
                        .toList(),
                proposal.getDrawingFiles().stream()
                        .map(ProposalDrawingResponse::from)
                        .toList()
        );
    }
}