package hanieum.conik.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 리프레시 토큰 갱신 요청 바디
 */
public record RefreshTokenRequest(
        @NotBlank(message = "refreshToken은 반드시 값이 있어야 합니다.")
        String refreshToken
) { }
