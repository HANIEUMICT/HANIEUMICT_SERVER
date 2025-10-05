package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EquipmentRequest(
        @NotBlank
        @Size(max=200)
        @Schema(description = "장비 이름", example = "CNC 선반", maxLength = 200)
        String name,

        @Size(max=10000)
        @Schema(description = "장비 설명 (null 가능)", example = "알루미늄 가공 가능 / 최대 Ø120mm", maxLength = 10_000, nullable = true)
        String description,

        @PositiveOrZero
        @Schema(description = "장비 수량(0 이상)", example = "3", minimum = "0", defaultValue = "0")
        Integer quantity,

        @NotEmpty(message = "이미지 URL은 최소 1개 이상이어야 합니다.")
        @ArraySchema(
                arraySchema = @Schema(
                        description = "장비 이미지 URL 목록",
                        example = "[\"https://cdn.example.com/equip/cnc-123.jpg\",\"https://cdn.example.com/equip/cnc-124.jpg\"]"
                ),
                schema = @Schema(type = "string", format = "uri", maxLength = 512, description = "이미지 URL")
        )

        List<
                @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
                @Size(max = 512, message = "이미지 URL은 512자 이하여야 합니다.")
                        String
                > imageUrl
) {}
