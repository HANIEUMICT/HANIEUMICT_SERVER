package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MemberEmailMarketingConsentUpdateRequest(
        @NotNull(message = "이메일 마케팅 수신동의 여부는 필수입니다.")
        @Schema(description = "이메일 마케팅 수신동의 여부", example = "true")
        Boolean isEmailMarketingAgreed
) {}
