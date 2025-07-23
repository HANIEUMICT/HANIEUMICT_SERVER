package hanieum.conik.adapter.project.webapi;

import hanieum.conik.adapter.project.dto.ProjectDrawingUploadRequest;
import hanieum.conik.application.project.provided.ProjectDrawingSaver;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "PROJECT")
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectDrawingSaver projectDrawingSaver;

    @Operation(summary = "프로젝트(공고) 도면 파일 업로드 API", description = "프로젝트(공고) 생성 중 도면 파일을 업로드합니다.")
    @PostMapping("/image")
    public ApiResponse<?> uploadImage(@RequestBody @Valid ProjectDrawingUploadRequest projectDrawingUploadRequest) {
        projectDrawingSaver.saveDrawingFileTemp(projectDrawingUploadRequest);
        return ApiResponse.success("도면 파일 업로드 성공");
    }
}
