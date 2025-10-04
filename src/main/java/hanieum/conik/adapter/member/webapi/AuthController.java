package hanieum.conik.adapter.member.webapi;

import hanieum.conik.application.member.provided.Auth;
import hanieum.conik.application.member.provided.TokenRefresh;
import hanieum.conik.domain.member.dto.*;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "AUTH", description = "회원가입/로그인 로직 API")
public class AuthController {
    private final Auth auth;
    private final TokenRefresh tokenRefresh;

    @Operation(summary = "[구현완료] 일반 회원가입", description = """
    ## 일반 회원 가입을 수행합니다.
    - 이메일, 비밀번호 등의 정보를 입력하여 회원가입합니다.
    - 회원가입 완료 시 로그인도 완료됩니다.
    """)
    @PostMapping("/signup/individual")
    public ApiResponse<MemberLoginResponse> signUpMember(@RequestBody @Valid MemberSignUpRequest request) {
        MemberLoginResponse loginResponse = auth.signUpIndividual(request);
        return ApiResponse.success(loginResponse);
    }

    @Operation(summary = "[구현완료] 기업 회원가입", description = """
    ## 기업 회원 가입을 수행합니다.
    - 기업을 선택/등록한 후에 회원가입을 진행합니다.
    - 이메일, 비밀번호 등의 정보를 입력하여 회원가입합니다.
    - 회원가입 완료 시 로그인도 완료됩니다.
    """)
    @PostMapping("/signup/company/{companyId}")
    public ApiResponse<MemberLoginResponse> signUpCompanyMember(@RequestBody @Valid MemberSignUpRequest request, @PathVariable Long companyId) {
        MemberLoginResponse loginResponse = auth.signUpCompanyMember(request, companyId);
        return ApiResponse.success(loginResponse);
    }

    @Operation(summary = "로그인", description = """
    ## 사용자 로그인을 수행합니다.
    - 입력 파라미터 : 이메일, 비밀번호
    - 리턴값 : accessToken, refreshToken, memberId
    """)
    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(@RequestBody @Valid MemberLoginRequest request) {
        MemberLoginResponse loginResponse = auth.login(request);
        return ApiResponse.success(loginResponse);
    }

    @Operation(
            summary = "토큰 갱신",
            description = """
        ## 리프레시 토큰을 사용해 새로운 Access/Refresh 토큰을 발급합니다.
        - 입력: { "refreshToken": "기존_리프레시_토큰" }
        - 반환: accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn
        """
    )
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        TokenResponse tokenResponse = tokenRefresh.refresh(request.refreshToken());
        return ApiResponse.success(tokenResponse);
    }
}
