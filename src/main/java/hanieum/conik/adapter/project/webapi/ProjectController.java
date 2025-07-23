package hanieum.conik.adapter.project.webapi;

import hanieum.conik.adapter.project.dto.ProjectDrawingUploadRequest;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.application.project.provided.ProjectSaver;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PROJECT")
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectDrawingSaver projectDrawingSaver;
    private final ProjectSaver projectSaver;

    @Operation(summary = "프로젝트(공고) 도면 파일 업로드 API", description = "프로젝트(공고) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("/image")
    public ApiResponse<?> uploadImage(@RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "초기 프로젝트(공고) 생성 API", description = "초기에 프로젝트(공고) 페이지를 생성합니다.")
    @PostMapping("/init")
    public ApiResponse<Long> initProject(@RequestParam("member_id") Long memberId) {
        return ApiResponse.success(projectSaver.create(memberId));
    }

    @Operation(summary = "프로젝트(공고) 수정 및 임시저장 API", description = "임시 저장 시, 발급된 프로젝트(공고)에 대해 정보를 수정합니다.")
    @PostMapping("/image2")
    public ApiResponse<?> saveProjectTemp(@RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }

    @Operation(summary = "프로젝트(공고) 저장 API", description = "작성 완료 된 프로젝트(공고)를 최종 저장합니다.")
    @PostMapping("/image3")
    public ApiResponse<?> saveProjectFinal(@RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }
}
