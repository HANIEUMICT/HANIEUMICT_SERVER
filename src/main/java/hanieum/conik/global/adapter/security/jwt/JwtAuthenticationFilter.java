package hanieum.conik.global.adapter.security.jwt;

import hanieum.conik.global.application.jwt.required.JwtTokenProviderPort;
import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProviderPort jwtTokenProviderPort;

    // JWT 검증을 제외할 경로
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            // Swagger UI
            "/swagger-ui.html",
            "/swagger-ui/",
            // OpenAPI JSON
            "/v1/api-docs",
            "/v1/api-docs/",
            "/v1/api-docs/swagger-config",
            // 인증 없이 접근할 Auth API
            "/v1/auth/signup",
            "/v1/auth/login",
            "/v1/email",
            "/v1/email/certificate"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(httpServletRequest);
        log.info("BearerToken: {}", token);

        try {
            if (token != null) {
                Authentication auth = jwtTokenProviderPort.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication set in SecurityContext.");
            }
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorType.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new AuthException(AuthErrorType.INVALID_FORMAT_TOKEN);
        } catch (Exception e) {
            throw new AuthException(AuthErrorType.SERVER_ERROR);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("BearerToken: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

