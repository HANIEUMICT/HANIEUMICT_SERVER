package hanieum.conik.application.member.provided;

import hanieum.conik.domain.member.dto.TokenResponse;

public interface TokenRefresh {
    /**
     * 리프레시 토큰을 검증하고, 새로운 Access/Refresh 토큰을 발급한다.
     *
     * @param refreshToken 클라이언트가 보낸 리프레시 토큰
     * @return 갱신된 AccessToken + RefreshToken
     */
    TokenResponse refresh(String refreshToken);
}
