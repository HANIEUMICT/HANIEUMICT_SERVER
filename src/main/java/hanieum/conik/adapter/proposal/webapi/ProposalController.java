package hanieum.conik.adapter.proposal.webapi;

import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalRegisterRequest;
import hanieum.conik.adapter.proposal.dto.response.ProposalDetailResponse;
import hanieum.conik.adapter.proposal.dto.response.ProposalResponse;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.enumerate.ProposalBidStatus;
import hanieum.conik.global.adapter.security.AuthSourceType;
import hanieum.conik.global.adapter.security.AuthorizeUser;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PROPOSAL")
@RequiredArgsConstructor
@RequestMapping("/v1/proposal")
public class ProposalController {
    private final ProposalSaver proposalSaver;
    private final ProposalFinder proposalFinder;
    private final ProposalDrawingSaver proposalDrawingSaver;

    @Operation(summary = "기업 견적서(입찰) 도면 파일 업로드 API", description = "기업 견적서(입찰) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("{memberId}/image")
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<?> uploadImage(@PathVariable("memberId") Long memberId,
                                      @RequestBody @Valid ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        proposalDrawingSaver.saveDrawingFileTemp(proposalDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "기업 견적서(입찰) 생성 API", description = "초기에 기업 견적서(입찰) 페이지를 생성합니다.")
    @PostMapping("/{projectId}/{memberId}/init")
    public ApiResponse<ProposalResponse> initProposal(@PathVariable("memberId") Long memberId,
                                                      @PathVariable("projectId") Long projectId) {
        Proposal proposal =  proposalSaver.initiate(memberId, projectId);

        return ApiResponse.success(ProposalResponse.from(proposal));
    }

    @Operation(summary = "기업 견적서(입찰) 수정 및 임시저장 API", description = "임시 저장 시, 발급된 기업 견적서(입찰)에 대해 정보를 수정합니다.")
    @PostMapping("{proposalId}/draft")
    public ApiResponse<ProposalResponse> saveProjectTemp(@PathVariable("proposalId") Long proposalId,
                                                         @RequestBody ProposalRegisterRequest proposalRegisterRequest) {
        Proposal proposal = proposalSaver.saveProposalDraft(proposalId, proposalRegisterRequest);
        return ApiResponse.success(ProposalResponse.from(proposal));
    }

    @Operation(summary = "기업 견적서(입찰) 수정 및 저장 API", description = "작성 완료 된 기업 견적서(입찰)를 최종 저장합니다.")
    @PostMapping("{proposalId}/final")
    public ApiResponse<ProposalResponse> saveProjectFinal(@PathVariable("proposalId") Long proposalId,
                                                          @RequestBody @Valid ProposalRegisterRequest proposalRegisterRequest) {

        Proposal proposal = proposalSaver.saveProposalFinal(proposalId, proposalRegisterRequest);
        return ApiResponse.success(ProposalResponse.from(proposal));
    }

    @Operation(summary = "프로젝트별 기업 견적서(입찰) 조회 API", description = "기업 견적서(입찰) 목록을 조회합니다.")
    @GetMapping("/{memberId}")
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<Page<ProposalDetailResponse>> getCompanyProposals(@PathVariable("memberId") Long memberId,
                                                                         @RequestParam(required = false) SubmitStatus status,
                                                                         @RequestParam(required = false) Long projectId,
                                                                         @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Proposal> proposals = proposalFinder.getCompanyProposals(memberId, projectId, status, pageable);

        return ApiResponse.success(proposals.map(ProposalDetailResponse::from));
    }


    @Operation(summary = "기업 견적서(입찰) 단일 조회 API", description = "특정 기업 견적서(입찰)를 조회합니다.")
    @GetMapping("/{proposalId}/detail")
    public ApiResponse<ProposalDetailResponse> getProposal(@PathVariable("proposalId") Long proposalId) {
        Proposal proposal = proposalFinder.getProposalDetail(proposalId);

        return ApiResponse.success(ProposalDetailResponse.from(proposal) ) ;
    }


    @Operation(summary = "기업 견적서(입찰) 요청 API", description = "입찰에 참여한 기업 중에 골라서 거래를 요청합니다.")
    @PatchMapping("/{proposalId}/request")
    public ApiResponse<ProposalResponse> dealRequest(@PathVariable("proposalId") Long proposalId) {
        Proposal proposal = proposalSaver.updateToDealRequested(proposalId);

        return ApiResponse.success(ProposalResponse.from(proposal));
    }

    @Operation(summary = "요청된 거래를 수락합니다.", description = "입찰에 참여한 기업 중에 골라서 거래를 요청합니다.")
    @PatchMapping("/{proposalId}/accept")
    public ApiResponse<ProposalResponse> acceptDeal(@PathVariable("proposalId") Long proposalId,
                                                    @RequestParam(required = false) ProposalBidStatus proposalBidStatus) {
        Proposal proposal = proposalSaver.acceptDeal(proposalId, proposalBidStatus);

        return ApiResponse.success(ProposalResponse.from(proposal));
    }

    @Operation(summary = "요청된 거래를 거절합니다.", description = "입찰한 견적서에 대해 수신한 거래 요청을 거절합니다.")
    @PatchMapping("/{proposalId}/reject")
    public ApiResponse<ProposalResponse> rejectDeal(@PathVariable("proposalId") Long proposalId,
                                                    @RequestParam(required = false) ProposalBidStatus proposalBidStatus) {
        Proposal proposal = proposalSaver.rejectDeal(proposalId, proposalBidStatus);

        return ApiResponse.success(ProposalResponse.from(proposal));
    }
}
