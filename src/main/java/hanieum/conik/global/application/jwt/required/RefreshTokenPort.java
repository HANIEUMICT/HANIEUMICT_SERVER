package hanieum.conik.global.application.jwt.required;

public interface RefreshTokenPort {
    void saveRefreshToken(Long memberId, String refreshToken, long expirationMinutes);
    String findRefreshToken(Long memberId);
    void deleteRefreshToken(Long memberId);
}
