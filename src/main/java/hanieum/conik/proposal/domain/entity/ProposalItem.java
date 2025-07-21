package hanieum.conik.proposal.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProposalItem extends AbstractEntity{
    private Proposal proposal;

    private String itemName;

    private String itemSize;

    private String itemNote;

    private Long itemUnitPrice;

    private int itemQuantity;

    public static ProposalItem create(String itemName, String itemSize, String itemNote,
                                      Long itemUnitPrice, int itemQuantity) {
        ProposalItem item = new ProposalItem();
        item.itemName = itemName;
        item.itemSize = itemSize;
        item.itemNote = itemNote;
        item.itemUnitPrice = itemUnitPrice;
        item.itemQuantity = itemQuantity;
        return item;
    }

    void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}