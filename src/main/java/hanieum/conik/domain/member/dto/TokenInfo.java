package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenInfo(
        @Schema(description = "액세스 토큰")
        String accessToken,

        @Schema(description = "리프레시 토큰")
        String refreshToken
) {
    public static TokenInfo of(String accessToken, String refreshToken) {
        return new TokenInfo(accessToken, refreshToken);
    }
}