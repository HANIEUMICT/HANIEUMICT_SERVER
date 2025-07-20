package hanieum.conik.proposal.domain.entity;

import hanieum.conik.global.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Proposal extends BaseEntity {
    private Long proposalId;

    private Long projectId;

    private Long companyId;

    private Long totalPrice;

    private Long firstPrice;

    private Long secondPrice;

    private String proposalDrawing;

    private String proposalNote;

    private List<ProposalItem> items = new ArrayList<>();

    public static Proposal create(Long projectId, Long companyId, Long totalPrice, Long firstPrice,
                                  Long secondPrice, String proposalDrawing, String proposalNote) {
        Proposal proposal = new Proposal();
        proposal.projectId = projectId;
        proposal.companyId = companyId;
        proposal.totalPrice = totalPrice;
        proposal.firstPrice = firstPrice;
        proposal.secondPrice = secondPrice;
        proposal.proposalDrawing = proposalDrawing;
        proposal.proposalNote = proposalNote;
        return proposal;
    }
}
