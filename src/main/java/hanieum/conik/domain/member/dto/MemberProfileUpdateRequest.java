package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberProfileUpdateRequest(
        @Schema(description = "이름", example = "윤도운")
        @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하여야 합니다.")
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String newPhoneNumber,

        @Schema(description = "새 비밀번호", example = "day6")
        String newPassword
){}