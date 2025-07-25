package hanieum.conik.application.proposal.provided;

import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalInitiateRequest;

public interface ProposalSaver {
    Long initiate(ProposalInitiateRequest proposalInitiateRequest);

    ProjectRegisterRequest saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    ProjectRegisterRequest saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest);
}
