package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MemberSmsMarketingConsentUpdateRequest(
        @NotNull(message = "전화번호 마케팅 수신동의 여부는 필수입니다.")
        @Schema(description = "전화번호 마케팅 수신동의 여부", example = "true")
        Boolean isSmsMarketingAgreed
) {}
