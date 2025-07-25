package hanieum.conik.adapter.proposal.webapi;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.adapter.proposal.dto.request.ProposalDrawingUploadRequest;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.proposal.provided.ProposalDrawingSaver;
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

    @Operation(summary = "기업 견적서(입찰) 도면 파일 업로드 API", description = "프로젝트(공고) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("{memberId}/image")
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<?> uploadImage(@PathVariable("memberId") Long memberId,
                                      @RequestBody @Valid ProposalDrawingUploadRequest proposalDrawingUploadRequest) {
        proposalDrawingSaver.saveDrawingFileTemp(proposalDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }
}
