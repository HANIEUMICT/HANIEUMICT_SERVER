package hanieum.conik.domain.proposal.domain.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Proposal extends AbstractEntity {
    private Long projectId;

    private Long companyId;

    private Long totalPrice;

    private Long firstPrice;

    private Long secondPrice;

    private String proposalNote;

    private List<ProposalItem> items = new ArrayList<>();

    private List<ProposalDrawingFile> drawingFiles = new ArrayList<>();

    public static Proposal create(Long projectId, Long companyId, Long totalPrice,
                                  Long firstPrice, Long secondPrice, String proposalNote) {
        Proposal proposal = new Proposal();
        proposal.projectId = projectId;
        proposal.companyId = companyId;
        proposal.totalPrice = totalPrice;
        proposal.firstPrice = firstPrice;
        proposal.secondPrice = secondPrice;
        proposal.proposalNote = proposalNote;
        return proposal;
    }

    /** 양방향 연관관계 편의 메서드 */
    public void addItem(ProposalItem item) {
        this.items.add(item);
        item.setProposal(this);
    }

    public void removeItem(ProposalItem item) {
        this.items.remove(item);
    }
}