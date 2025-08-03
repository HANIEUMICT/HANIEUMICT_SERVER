package hanieum.conik.domain.proposal.domain.entity;

import hanieum.conik.adapter.proposal.dto.request.ProposalItemRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.ProjectBidStatus;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.enumerate.ProposalBidStatus;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private SubmitStatus submitStatus = SubmitStatus.INITIALIZE;

    private ProposalBidStatus proposalBidStatus = ProposalBidStatus.PRE_BID;

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

    public static Proposal initiate(Member member, Project project) {
        Proposal proposal = new Proposal();
        proposal.projectId = project.getId();
        proposal.companyId = member.getCompanyId();
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

    public void update(ProposalRegisterRequest proposalRegisterRequest) {
        this.projectId = proposalRegisterRequest.projectId();
        this.companyId = proposalRegisterRequest.companyId();
        this.totalPrice = proposalRegisterRequest.totalPrice();
        this.firstPrice = proposalRegisterRequest.firstPrice();
        this.secondPrice = proposalRegisterRequest.secondPrice();
        this.proposalNote = proposalRegisterRequest.proposalNote();

        this.items.clear();

        for (ProposalItemRequest proposalItemRequest : proposalRegisterRequest.items()) {
            ProposalItem item = proposalItemRequest.toProposalItem();
            this.addItem(item);
        }
    }

    public void updateToDraft() {
        this.submitStatus = SubmitStatus.TEMPORARY_SAVE;
    }

    public void updateToFinal() {
        this.submitStatus = SubmitStatus.SUBMIT;
    }

    public void updateToBidRequested() {
        this.proposalBidStatus = ProposalBidStatus.DEAL_REQUESTED;
    }

    public void acceptDeal() { this.proposalBidStatus = ProposalBidStatus.ACCEPT_DEAL; }

    public void rejectDeal() { this.proposalBidStatus = ProposalBidStatus.REJECT_DEAL; }

    public void updateToBidRejected() {
        this.proposalBidStatus = ProposalBidStatus.BID_REJECTED;
    }
}