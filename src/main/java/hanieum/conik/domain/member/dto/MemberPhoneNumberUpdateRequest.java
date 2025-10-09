package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberPhoneNumberUpdateRequest(
        @NotBlank(message = "전화번호는 빈 값이거나 null 일 수 없습니다.")
        @Schema(description = "전화번호", example = "010-1234-5678")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String newPhoneNumber
) {}
