package hanieum.conik.adapter.project.webapi;

import hanieum.conik.adapter.project.dto.request.BidStatusUpdateRequest;
import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.adapter.project.dto.request.ProjectListResponse;
import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.adapter.project.dto.response.ProjectWithProposalsResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.ProgressStatus;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.adapter.security.AuthSourceType;
import hanieum.conik.global.adapter.security.AuthorizeUser;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PROJECT")
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectDrawingSaver projectDrawingSaver;
    private final ProjectSaver projectSaver;
    private final ProjectFinder projectFinder;
    private final MemberFinder memberFinder;

    @Operation(summary = "프로젝트(공고) 도면 파일 업로드 API", description = "프로젝트(공고) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("{memberId}/image") // TODO : 여기 memberId를 받는 이유가 뭔가요? 왜 로그인된 사용자에 한해서 하지 않고 ,,
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<?> uploadImage(
            @PathVariable("memberId") Long memberId,
          @RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest
    ) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "초기 프로젝트(공고) 생성 API", description = "초기에 프로젝트(공고) 페이지를 생성합니다.")
    @PostMapping("{memberId}/init") // TODO : 여기 memberId를 받는 이유가 뭔가요? 왜 로그인된 사용자에 한해서 하지 않고 ,,
    @AuthorizeUser(sourceType = AuthSourceType.PATH_VARIABLE, paramName = "memberId")
    public ApiResponse<ProjectDetailResponse> initProject(
            @PathVariable("memberId") Long memberId
    ) {
        Project project = projectSaver.initiate(memberId);

        return ApiResponse.success(ProjectDetailResponse.from(project));
    }

    @Operation(summary = "프로젝트(공고) 수정 및 임시저장 API", description = "임시 저장 시, 발급된 프로젝트(공고)에 대해 정보를 수정합니다.")
    @PostMapping("{projectId}/draft") // TODO : 여기 memberId를 받는 이유가 뭔가요? 왜 로그인된 사용자에 한해서 하지 않고 ,,
    @AuthorizeUser(sourceType = AuthSourceType.REQUEST_BODY, fieldName = "memberId")
    public ApiResponse<ProjectDetailResponse> saveProjectTemp(
            @PathVariable("projectId") Long projectId,
            @RequestBody ProjectRegisterRequest projectRegisterRequest
    ) {
        Project project = projectSaver.saveProjectDraft(projectId, projectRegisterRequest);

        return ApiResponse.success(ProjectDetailResponse.from(project));
    }

    @Operation(summary = "프로젝트(공고) 수정 및 저장 API", description = "작성 완료 된 프로젝트(공고)를 최종 저장합니다.")
    @PostMapping("{projectId}/final") // TODO : 여기 memberId를 받는 이유가 뭔가요? 왜 로그인된 사용자에 한해서 하지 않고 ,,
    @AuthorizeUser(sourceType = AuthSourceType.REQUEST_BODY, fieldName = "memberId")
    public ApiResponse<ProjectDetailResponse> saveProjectFinal(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid ProjectRegisterRequest projectRegisterRequest
    ) {
        Project project = projectSaver.saveProjectFinal(projectId, projectRegisterRequest);

        return ApiResponse.success(ProjectDetailResponse.from(project));
    }

    @Operation(summary = "프로젝트(공고) 목록 조회 API", description = """
    ## 프로젝트(공고) 목록을 조회합니다.
    - status는 입력하지 않을 시, 임시저장 / 최종 저장된 견적서 목록이 조회됩니다.
    - memberId는 입력하지 않을 시, 전체 사용자의 견적서 대상으로 검색합니다.
    - progressStatus는 입력하지 않을 시, 전체 견적서 대상으로 검색합니다.
    """)
    @GetMapping
    public ApiResponse<ProjectListResponse> getMemberProjects(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam(value = "status", required = false) SubmitStatus submitStatus,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "progressStatus", required = false) ProgressStatus progressStatus,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ApiResponse.success(projectFinder.getMemberProjects(authDetails, memberId, submitStatus, progressStatus, pageable));
    }

    @Operation(summary = "특정 프로젝트(공고) 조회 API", description = "프로젝트 ID로 특정 프로젝트를 조회합니다.")
    @GetMapping("/{projectId}/detail")
    public ApiResponse<ProjectWithProposalsResponse> getProject(
            @PathVariable("projectId") Long projectId
    ) {

        return ApiResponse.success(projectFinder.getProjectDetailWithProposals(projectId));
    }

    @Operation(summary = "프로젝트(공고) 입찰 상태 변경 API", description = "프로젝트(공고)의 입찰 상태를 변경합니다.")
    @PatchMapping("/{projectId}/status")
    @AuthorizeUser(sourceType = AuthSourceType.REQUEST_BODY, fieldName = "memberId")
    public ApiResponse<ProjectDetailResponse> changeProjectBidStatus(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid  BidStatusUpdateRequest bidStatusUpdateRequest
    ) {
        Project project = projectSaver.updateProjectBidStatus(projectId, bidStatusUpdateRequest);

        return ApiResponse.success(ProjectDetailResponse.from(project));
    }

    @Operation(summary = "기업 마이페이지 - 나에게 온 견적서 목록 조회", description = """
    ## 기업 회원이 자신의 기업에 전송된 프로젝트(공고) 목록을 조회합니다.
    - 자신의 기업에 전송된 프로젝트(공고) 목록을 조회합니다.
    """)
    @GetMapping("/me/company")
    public ApiResponse<Page<ProjectDetailResponse>> getProjectsByCompanyId(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Long companyId = memberFinder.findCompanyIdByMemberId(authDetails.getMemberId());

        return ApiResponse.success(projectFinder.findProjectsByCompanyId(companyId, pageable));
    }
}
