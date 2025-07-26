package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.MemberProposalResponse;

public interface ProposalSaver {
    MemberProposalResponse initiate(Long memberId);

    ProjectRegisterRequest saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    ProjectRegisterRequest saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest);
}
