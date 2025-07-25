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

    public void addItem(ProposalItem item) {
        this.items.add(item);
        item.updateProposal(this);
    }

    public void removeItem(ProposalItem item) {
        this.items.remove(item);
    }

    public void addDrawing(ProposalDrawingFile drawingFile) {
        this.drawingFiles.add(drawingFile);
        drawingFile.updateProposal(this);
    }

    public void removeDrawing(ProposalDrawingFile drawingFile) {
        this.drawingFiles.remove(drawingFile);
    }
}