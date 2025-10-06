package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PortfolioRequest(
        @Schema(description = "보유 수량", example = "5", minimum = "0")
        @Min(0) Integer quantity,

        @Schema(description = "포트폴리오 설명", example = "정밀 가공용 CNC 장비 제작 사례입니다.")
        String description,


        @NotEmpty(message = "포트폴리오 이미지는 최소 1개 이상이어야 합니다.")
        @ArraySchema(
                arraySchema = @Schema(
                        description = "포트폴리오 이미지 URL 목록",
                        example = "[\"https://cdn.example.com/portfolio/cnc-1.jpg\", \"https://cdn.example.com/portfolio/cnc-2.jpg\"]"
                )
        )
        List<@NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
            @Size(max = 1024, message = "이미지 URL은 1024자 이하여야 합니다.")
            String
            > imageUrls,

        @Schema(description = "포트폴리오 카테고리", example = "기계가공")
        @NotBlank
        String category
) {}
