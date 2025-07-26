package hanieum.conik.adapter.proposal.webapi;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalInitiateRequest;
import hanieum.conik.adapter.proposal.dto.response.MemberProposalResponse;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalSaver;
import hanieum.conik.global.adapter.security.AuthSourceType;
import hanieum.conik.global.adapter.security.AuthorizeUser;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PROPOSAL")
@RequiredArgsConstructor
@RequestMapping("/v1/proposal")
public class ProposalController {
    private final ProposalDrawingSaver proposalDrawingSaver;
    private final ProposalSaver proposalSaver;

    @Operation(summary = "기업 견적서(입찰) 도면 파일 업로드 API", description = "기업 견적서(입찰) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("{memberId}/image")
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<?> uploadImage(@PathVariable("memberId") Long memberId,
                                      @RequestBody @Valid ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        proposalDrawingSaver.saveDrawingFileTemp(proposalDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "기업 견적서(입찰) 생성 API", description = "초기에 기업 견적서(입찰) 페이지를 생성합니다.")
    @PostMapping("/{memberId}/init")
    @AuthorizeUser(sourceType = AuthSourceType.NONE)
    public ApiResponse<MemberProposalResponse> initProposal(@PathVariable("memberId") Long memberId) {
        return ApiResponse.success(proposalSaver.initiate(memberId));
    }
}
