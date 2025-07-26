package hanieum.conik.adapter.proposal.dto.request;

import hanieum.conik.domain.proposal.domain.entity.ProposalItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProposalItemRequest(
        @NotBlank(message = "품명은 필수입니다")
        String itemName,

        String itemSize,

        String itemNote,

        @NotNull(message = "단가는 필수입니다")
        @Min(value = 0, message = "단가는 0 이상이어야 합니다")
        Long itemUnitPrice,

        @NotNull(message = "수량은 필수입니다")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        Integer itemQuantity
) {
        public static ProposalItemRequest from(ProposalItem item) {
                return new ProposalItemRequest(
                        item.getItemName(),
                        item.getItemSize(),
                        item.getItemNote(),
                        item.getItemUnitPrice(),
                        item.getItemQuantity()
                );
        }
}
