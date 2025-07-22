package hanieum.conik.global.adapter.security.jwt;

import hanieum.conik.global.application.jwt.required.JwtTokenProviderPort;
import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements JwtTokenProviderPort {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String SECRETKEY;

    @Value("${jwt.token.access-expiration-time}")
    private long accessTokenExpiration;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshTokenExpiration;

    @Override
    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiration);
    }

    @Override
    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshTokenExpiration);
    }

    @Override
    public Long parseRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRETKEY)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 토큰이 만료되었거나 서명이 잘못된 경우 예외 발생
            if (claims.getExpiration().before(new Date())) {
                throw new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
            }
            return Long.valueOf(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to parse refresh token: {}", e.getMessage());
            throw new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    @Override
    public Authentication getAuthentication(String token) {
        if (!isValidToken(token)) {
            throw new AuthException(AuthErrorType.INVALID_ACCESS_TOKEN);
        }

        Long memberId = getMemberIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberId.toString());

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    private boolean isValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRETKEY)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(Long memberId, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRETKEY)
                .compact();
    }

    private Long getMemberIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
