package hanieum.conik.adapter.member.webapi;

import hanieum.conik.adapter.member.dto.MemberLoginRequest;
import hanieum.conik.adapter.member.dto.MemberLoginResponse;
import hanieum.conik.adapter.member.dto.MemberSignUpRequest;
import hanieum.conik.application.member.AuthService;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "[구현완료] 일반 회원가입", description = """
    ## 일반 회원 가입을 수행합니다.
    - 이메일, 비밀번호 등의 정보를 입력하여 회원가입합니다.
    - 회원가입 완료 시 로그인도 완료됩니다.
    """)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberLoginResponse>> signUp(@RequestBody @Valid MemberSignUpRequest request) {
        MemberLoginResponse loginResponse = authService.register(request);
        return new ResponseEntity<>(ApiResponse.success(loginResponse), HttpStatus.OK);
    }

    @Operation(summary = "일반 로그인", description = """
    ## 일반 사용자 로그인을 수행합니다.
    - 입력 파라미터 : 이메일, 비밀번호
    - 리턴값 : accessToken, refreshToken, memberId
    """)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginResponse>> login(@RequestBody @Valid MemberLoginRequest request) {
        MemberLoginResponse loginResponse = authService.login(request);
        return new ResponseEntity<>(ApiResponse.success(loginResponse),HttpStatus.OK);
    }
}
