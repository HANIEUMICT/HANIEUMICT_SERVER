package hanieum.conik.domain.company.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Schema(description = "포트폴리오 항목(스냅샷 PUT용). id가 있으면 유지(수정 불가), 없으면 신규 추가")
public record PortfolioUpdateRequest(
        @Schema(description = "기존 포트폴리오 ID (신규 등록 시 null)")
        Long id,

        @Schema(description = "생산 수량", example = "100")
        @PositiveOrZero
        Integer quantity,

        @Schema(description = "포트폴리오 설명", example = "브라켓 가공 프로젝트")
        String description,

        @Schema(description = "포트폴리오 이미지 URL 목록", example = "[\"https://cdn.example.com/pf/bracket-1.jpg\"]")
        List<@NotBlank @URL @Size(max = 2048) String> imageUrls,

        @Schema(description = "작업 카테고리", example = "가공")
        String category
) {}