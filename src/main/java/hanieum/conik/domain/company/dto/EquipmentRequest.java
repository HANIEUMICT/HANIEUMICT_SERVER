package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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

        @NotBlank
        @Size(max=512)
        @Schema(description = "장비 이미지 URL", example = "https://cdn.example.com/equip/cnc-123.jpg", maxLength = 512, format = "uri")
        String imageUrl
) {}
