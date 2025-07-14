package hanieum.conik.global.adapter.webapi;

import hanieum.conik.global.adapter.s3.dto.ImageUploadRequest;
import hanieum.conik.global.adapter.s3.dto.ReadPreSignedUrlResponse;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import hanieum.conik.global.application.required.BucketClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bucket")
@Tag(name = "USER", description = "사용자 도메인 API")
public class ImageController {
    private final BucketClient bucketClient;

    @Operation(summary = "파일 업로드 URl 발급", description = "파일 업로드를 위한 presigned URL을 발급합니다.")
    @PostMapping("/presigned")
    public ApiResponse<?> getPresignedUrl(@RequestBody ImageUploadRequest imageUploadRequest) {
        ReadPreSignedUrlResponse response = bucketClient.getPreSignedUrl(imageUploadRequest);
        return ApiResponse.success(response);
    }
}
