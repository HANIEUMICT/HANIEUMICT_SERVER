package hanieum.conik.adapter.member.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SmsCertificateRequest(
        @NotBlank
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        @Schema(description = "인증코드를 보낼 휴대폰 번호", example = "010-1234-5678")
        String phoneNumber,

        @NotBlank
        @Schema(description = "인증 코드", example = "123456")
        String authCode
){};
