package hanieum.conik.global.application.jwt.required;

import org.springframework.security.core.Authentication;

/**
 * JWT 토큰을 생성·검증하기 위한 포트 인터페이스
 */
public interface JwtTokenProviderPort {

    /**
     * 주어진 회원 ID와 역할을 담은 Access Token(JWT)을 생성합니다.
     *
     * @param memberId 회원 식별자
     * @return 발급된 Access Token 문자열
     */
    String createAccessToken(Long memberId);

    /**
     * 주어진 회원 ID와 역할을 담은 Refresh Token(JWT)을 생성합니다.
     *
     * @param memberId 회원 식별자
     * @return 발급된 Refresh Token 문자열
     */
    String createRefreshToken(Long memberId);


    /**
     * 토큰을 파싱·검증하고 Spring Security Authentication 객체를 생성
     * @param token JWT 문자열
     * @return 인증 정보(Authentication)
     * @throws AuthException 검증 실패 시 던져짐
     */
    Authentication getAuthentication(String token);

    /**
     * 파라미터로 넘어온 Refresh Token 을 파싱·검증한 뒤,
     * 토큰의 subject(회원 ID)를 반환합니다.
     *
     * @param refreshToken 리프레시 토큰 문자열
     * @return 토큰에 담긴 회원 ID
     * @throws JwtException 토큰이 유효하지 않거나 만료된 경우 던져집니다.
     */
    Long parseRefreshToken(String refreshToken);

    Long getRefreshTokenExpiration();
}
