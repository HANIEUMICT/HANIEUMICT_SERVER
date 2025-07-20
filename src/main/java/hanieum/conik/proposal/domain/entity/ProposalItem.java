package hanieum.conik.proposal.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProposalItem {
    private Long proposalItemId;

    private Proposal proposal;

    private Long itemName;

    private String itemSize;

    private String itemNote;

    private Long itemUnitPrice;

    private int itemQuantity;

    public static ProposalItem create(Long itemName, String itemSize, String itemNote,
                                      Long itemUnitPrice, int itemQuantity) {
        ProposalItem item = new ProposalItem();
        item.itemName = itemName;
        item.itemSize = itemSize;
        item.itemNote = itemNote;
        item.itemUnitPrice = itemUnitPrice;
        item.itemQuantity = itemQuantity;
        return item;
    }
}