package hanieum.conik.adapter.project.webapi;

import hanieum.conik.adapter.project.dto.request.ProjectDrawingUploadRequest;
import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.adapter.project.dto.response.MemberProjectQueryResponse;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PROJECT")
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectDrawingSaver projectDrawingSaver;
    private final ProjectSaver projectSaver;
    private final ProjectFinder projectFinder;

    @Operation(summary = "프로젝트(공고) 도면 파일 업로드 API", description = "프로젝트(공고) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("/image")
    public ApiResponse<?> uploadImage(@RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "초기 프로젝트(공고) 생성 API", description = "초기에 프로젝트(공고) 페이지를 생성합니다.")
    @PostMapping("{memberId}/init")
    public ApiResponse<Long> initProject(@PathVariable("memberId") Long memberId) {
        return ApiResponse.success(projectSaver.initiate(memberId));
    }

    @Operation(summary = "프로젝트(공고) 수정 및 임시저장 API", description = "임시 저장 시, 발급된 프로젝트(공고)에 대해 정보를 수정합니다.")
    @PostMapping("{projectId}/draft")
    public ApiResponse<ProjectRegisterRequest> saveProjectTemp(@PathVariable("projectId") Long projectId,
                                          @RequestBody ProjectRegisterRequest projectRegisterRequest) {
        return ApiResponse.success(projectSaver.saveProjectDraft(projectId, projectRegisterRequest));
    }

    @Operation(summary = "프로젝트(공고) 저장 API", description = "작성 완료 된 프로젝트(공고)를 최종 저장합니다.")
    @PostMapping("{projectId}/final")
    public ApiResponse<ProjectRegisterRequest> saveProjectFinal(@PathVariable("projectId") Long projectId,
                                           @RequestBody @Valid ProjectRegisterRequest projectRegisterRequest) {
        return ApiResponse.success(projectSaver.saveProjectFinal(projectId, projectRegisterRequest));
    }

    @Operation(summary = "사용자 프로젝트(공고) 조회 API", description = "사용자의 프로젝트(공고) 목록을 조회합니다.")
    @GetMapping("/{memberId}")
    public ApiResponse<List<MemberProjectQueryResponse>> getMemberProjects(@PathVariable("memberId") Long memberId,
                                                                           @RequestParam(value = "status", required = false) String status) {
        return ApiResponse.success(projectFinder.getMemberProjects(memberId, status));
    }
}
