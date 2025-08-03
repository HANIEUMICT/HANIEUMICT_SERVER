package hanieum.conik.adapter.proposal.dto.request;

import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.enumerate.ProposalBidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProposalRegisterRequest(
        @NotNull(message = "프로젝트 ID는 필수입니다")
        Long projectId,

        @NotNull(message = "회사 ID는 필수입니다")
        Long companyId,

        @NotNull(message = "견적 총액은 필수입니다")
        @Min(value = 0, message = "견적 총액은 0 이상이어야 합니다")
        Long totalPrice,

        @NotNull(message = "1차 지급액은 필수 입력값입니다.")
        Long firstPrice,

        @NotNull(message = "2차 지급액은 필수 입력값입니다.")
        Long secondPrice,

        String proposalNote,

        @NotEmpty(message = "견적 항목은 최소 1개 이상이어야 합니다")
        @Valid
        List<ProposalItemRequest> items,

        @NotNull
        @Schema(description = "작성 상태", example = "INITIALIZE | TEMPORARY_SAVE | SUBMIT")
        ProposalBidStatus proposalBidStatus,

        @NotNull
        @Schema(description = "작성 상태", example = "INITIALIZE | TEMPORARY_SAVE | SUBMIT")
        SubmitStatus submitStatus
) {
    public static ProposalRegisterRequest from(Proposal proposal) {
        return new ProposalRegisterRequest(
                proposal.getProjectId(),
                proposal.getCompanyId(),
                proposal.getTotalPrice(),
                proposal.getFirstPrice(),
                proposal.getSecondPrice(),
                proposal.getProposalNote(),
                proposal.getItems().stream()
                        .map(ProposalItemRequest::from)
                        .toList(),
                proposal.getProposalBidStatus(),
                proposal.getSubmitStatus()
        );
    }
}
