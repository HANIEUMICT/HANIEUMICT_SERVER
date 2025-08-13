package hanieum.conik.adapter.proposal.dto.response;

import hanieum.conik.adapter.company.webapi.response.CompanyThumbnailResponse;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.proposal.domain.entity.Proposal;

import java.time.LocalDate;

public record ProposalThumbnailResponse(

        Long proposalId,

        CompanyThumbnailResponse companyThumbnailResponse,

        Long totalPrice,

        LocalDate operateUntil
) {
    public static ProposalThumbnailResponse from(Proposal proposal, Company company) {
        return new ProposalThumbnailResponse(
                proposal.getId(),
                CompanyThumbnailResponse.from(company),
                proposal.getTotalPrice(),
                proposal.getOperateUntil()
        );
    }
}
