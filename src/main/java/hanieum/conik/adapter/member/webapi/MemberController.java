package hanieum.conik.adapter.member.webapi;

import hanieum.conik.application.member.MemberModifyService;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.dto.PasswordChangeRequest;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Validated
@Tag(name = "MEMBER", description = "회원 정보를 조회하고 수정하는 API")
public class MemberController {

    private final MemberModifyService memberModifyService;

    @Operation(summary = "회원 정보 수정", description = """
    ## 회원 정보 수정을 수행합니다.
    - 사용자의 전화번호를 입력으로 받아 수정합니다. => 전화번호 인증 로직도 추가할 예정입니다. 
    """)
    @PutMapping("/profile")
    public ApiResponse<?> updateMemberProfile(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberProfileUpdateRequest request
    ) {
        memberModifyService.updateProfile(authDetails.getMemberId(), request);
        return ApiResponse.success();
    }

    @Operation(summary = "비밀번호 수정", description = """
    ## 비밀번호 수정을 수행합니다.
    - 현재 비밀번호와 변경할 새로운 비밀번호를 입력합니다. 
    - 현재 비밀번호가 회원의 비밀번호와 일치하는지 확인합니다.
    - 일치하면 입력받은 새로운 비밀번호로 회원의 비밀번호를 업데이트하게 됩니다. 
    """)
    @PutMapping("/password")
    public ApiResponse<?> updatePassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid PasswordChangeRequest request
    ){
        memberModifyService.updatePassword(authDetails.getMemberId(), request);
        return ApiResponse.success();
    }

    @Operation(summary = "주소 추가", description = """
    ## 주소 추가를 수행합니다.
    - 사용자가 주소를 추가할 수 있습니다.
    """)
    @PutMapping("/addresses")
    public ApiResponse<?> addAddress(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid AddressRegisterRequest request
    ) {
        memberModifyService.addAddress(authDetails.getMemberId(), request);
        return ApiResponse.success();
    }
}