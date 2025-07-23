package hanieum.conik.global.config;

import hanieum.conik.global.adapter.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // REST API에서는 CSRF 보호 불필요 (세션을 쓰지 않으므로)
                .httpBasic(AbstractHttpConfigurer::disable) // 브라우저에서 뜨는 기본 인증 팝업을 막음
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable) // 로그인 form 비활성화 (우린 토큰 기반이니까)
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 기반 인증이므로 세션을 아예 생성하지 않음
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.OPTIONS, "/v1/**").permitAll()
                                .requestMatchers("/actuator/health/**").permitAll()
                                .requestMatchers(
                                        "/v1/auth/signup",
                                        "/v1/auth/login",
                                        "/v1/email",
                                        "/v1/email/certificate"
                                ).permitAll()
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v1/api-docs/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * BCryptPasswordEncoder
     * - 비밀번호 해시화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
