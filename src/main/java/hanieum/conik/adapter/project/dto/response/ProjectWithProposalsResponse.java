package hanieum.conik.adapter.project.dto.response;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.adapter.proposal.dto.response.ProposalThumbnailResponse;

import java.util.List;

public record ProjectWithProposalsResponse (
    ProjectDetailResponse projectDetailResponse,

    List<ProposalThumbnailResponse> proposalThumbnails,

    MemberAddressResponse memberAddressResponse
){ }
