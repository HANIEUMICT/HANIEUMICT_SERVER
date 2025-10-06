package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberNameUpdateRequest(
        @NotBlank(message = "이름은 빈 값이거나 null 일 수 없습니다.")
        @Schema(description = "이름", example = "부승관")
        @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하여야 합니다.")
        String name
) {}
