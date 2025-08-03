package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.domain.proposal.domain.entity.ProposalItem;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProposalItemResponse(
        @Schema(description = "견적 항목 ID", example = "1")
        Long itemId,

        @Schema(description = "품명", example = "스테인리스 파이프")
        String itemName,

        @Schema(description = "규격", example = "50mm x 2m")
        String itemSize,

        @Schema(description = "비고", example = "고품질 재료")
        String itemNote,

        @Schema(description = "단가", example = "50000")
        Long itemUnitPrice,

        @Schema(description = "수량", example = "10")
        Integer itemQuantity,

        @Schema(description = "총금액", example = "500000")
        Long totalAmount
) {
    public static ProposalItemResponse from(ProposalItem item) {
        return new ProposalItemResponse(
                item.getId(),
                item.getItemName(),
                item.getItemSize(),
                item.getItemNote(),
                item.getItemUnitPrice(),
                item.getItemQuantity(),
                item.getItemUnitPrice() * item.getItemQuantity()
        );
    }
}