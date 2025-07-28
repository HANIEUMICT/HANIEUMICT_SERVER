package hanieum.conik.application.project.provided;

import hanieum.conik.adapter.project.dto.request.BidStatusUpdateRequest;
import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.domain.proposal.domain.enumerate.BidStatus;

public interface ProjectSaver {
    /**
     * 프로젝트를 초기화합니다.
     * @param memberId 프로젝트(공고)를 작성자 ID
     * @return 생성된 프로젝트의 ID
     */
    MemberProjectQueryResponse initiate(Long memberId);

    /**
     * 프로젝트를 임시 저장합니다.
     * @param projectId 프로젝트 ID
     * @param projectRegisterRequest 프로젝트 등록 요청 정보
     * @return 저장된 프로젝트 정보
     */
    MemberProjectQueryResponse saveProjectDraft(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    /**
     * 프로젝트를 최종 저장합니다.
     * @param projectId 프로젝트 ID
     * @param projectRegisterRequest 프로젝트 등록 요청 정보
     * @return 저장된 프로젝트 정보
     */
    MemberProjectQueryResponse saveProjectFinal(Long projectId, ProjectRegisterRequest projectRegisterRequest);

    /**
     * 프로젝트의 입찰 상태를 변경합니다.
     * @param projectId 프로젝트 ID
     * @param bidStatusUpdateRequest 변경할 입찰 상태
     * @return 변경된 프로젝트 정보
     */
    MemberProjectQueryResponse updateProjectBidStatus(Long projectId, BidStatusUpdateRequest bidStatusUpdateRequest);
}
