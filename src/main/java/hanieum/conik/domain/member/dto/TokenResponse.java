package hanieum.conik.domain.member.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn,   // 남은 유효시간 (초)
        long refreshTokenExpiresIn   // 남은 유효시간 (초)
) {}
