package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Schema(description = "장비 항목(스냅샷 PUT용). id가 있으면 유지(수정 불가), 없으면 신규 추가")
public record EquipmentUpdateRequest(
        @Schema(description = "기존 장비 ID (신규 등록 시 null)")
        Long id,

        @Schema(description = "장비명", example = "CNC 선반")
        @NotBlank
        String name,

        @Schema(description = "장비 설명", example = "알루미늄 가공용 장비")
        String description,

        @Schema(description = "보유 수량", example = "2")
        @PositiveOrZero
        Integer quantity,

        @Schema(description = "장비 이미지 URL 목록", example = "[\"https://cdn.example.com/equip/cnc-1.jpg\"]")
        List<@NotBlank @URL @Size(max = 2048) String> imageUrls
) {
}