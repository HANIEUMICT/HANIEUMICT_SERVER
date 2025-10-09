package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberPasswordUpdateRequest (
        @NotBlank(message = "기존 비밀번호는 빈 값이거나 null 일 수 없습니다.")
        @Schema(description = "현재 비밀번호", example = "day6")
        String currentPassword,

        @NotBlank(message = "새로운 비밀번호는 빈 값이거나 null 일 수 없습니다.")
        @Schema(description = "새 비밀번호", example = "seventeen17!")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|\\-=\\[\\]{};:',.<>/?])[A-Za-z\\d!@#$%^&*()_+|\\-=\\[\\]{};:',.<>/?]{8,20}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
        )
        String newPassword
) {}
