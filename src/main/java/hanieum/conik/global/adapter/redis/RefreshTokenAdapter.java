package hanieum.conik.global.adapter.redis;

import hanieum.conik.global.application.jwt.required.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenPort {

    private final RefreshTokenService refreshTokenService;


    @Override
    public void saveRefreshToken(Long memberId, String refreshToken, long expirationMinutes) {
        refreshTokenService.saveRefreshToken(memberId, refreshToken, expirationMinutes);
    }

    @Override
    public String findRefreshToken(Long memberId) {
        return refreshTokenService.getRefreshToken(memberId);
    }

    @Override
    public void deleteRefreshToken(Long memberId) {
        refreshTokenService.deleteRefreshToken(memberId);
    }
}
