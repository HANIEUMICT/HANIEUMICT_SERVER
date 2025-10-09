package hanieum.conik.adapter.member.webapi;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.adapter.member.dto.MemberInfoResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.provided.MemberSaver;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.dto.*;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Validated
@Tag(name = "MEMBER", description = "회원 정보를 조회하고 수정하는 API")
public class MemberController {

    private final MemberSaver memberSaver;
    private final MemberFinder memberFinder;

    @Operation(summary = "회원 정보 수정", description = """
    ## 회원 정보 수정을 수행합니다.
    - 사용자의 이름, 전화번호, 비밀번호, 이용약관 정보를 수정합니다.
    """)
    @PatchMapping("/profile") // TODO : 이거 공장 마이페이지 떄문에 만들었던 건가요?! UI가 이후에 수정된 거 같은데 안 쓰면 없애도 될 거 같아서용!
    public ApiResponse<?> updateMemberProfile(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberProfileUpdateRequest request
    ) {
        memberSaver.updateProfile(authDetails.getMemberId(), request);
        return ApiResponse.success("회원 정보 수정이 완료되었습니다.");
    }

    @Operation(summary = "내 정보 조회", description = """
    ## 내 정보를 조회합니다.
    - 사용자의 기본 정보를 조회할 수 있습니다.
    """)
    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMemberInfo(
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        return ApiResponse.success(memberFinder.getMemberInfo(authDetails.getMemberId()));
    }

    @Operation(summary = "회원 정보 - 이름 수정", description = """
    ## 회원 정보 - 이름 수정을 수행합니다.
    - 사용자의 이름을 변경합니다.
    """)
    @PatchMapping("/me/name")
    public ApiResponse<String> updateMemberName(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberNameUpdateRequest request
    ) {
        memberSaver.updateName(authDetails.getMemberId(), request);
        return ApiResponse.success("회원 이름 수정이 완료되었습니다.");
    }

    @Operation(summary = "회원 정보 - 비밀번호 수정", description = """
    ## 회원 정보 - 비밀번호 수정을 수행합니다.
    - 사용자의 비밀번호를 변경합니다.
    """)
    @PatchMapping("/me/password")
    public ApiResponse<String> updateMemberPassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberPasswordUpdateRequest request
    ) {
        memberSaver.updatePassword(authDetails.getMemberId(), request);
        return ApiResponse.success("회원 비밀번호 수정이 완료되었습니다.");
    }

    @Operation(summary = "회원 정보 - 전화번호 수정", description = """
    ## 회원 정보 - 전화번호 수정을 수행합니다.
    - 사용자의 전화번호를 인증 후 변경합니다.
    """)
    @PatchMapping("/me/phone-number")
    public ApiResponse<String> updateMemberPhoneNumber(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberPhoneNumberUpdateRequest request
    ) {
        memberSaver.updatePhoneNumber(authDetails.getMemberId(), request);
        return ApiResponse.success("회원 전화번호 수정이 완료되었습니다.");
    }

    @Operation(summary = "회원 정보 - 이메일 혜택/이벤트 정보 알림 수신 동의 여부 수정", description = """
    ## 회원 정보 -  이메일 혜택/이벤트 정보 알림 수신 동의 여부 수정을 수행합니다.
    - 사용자의 이메일 혜택/이벤트 정보 알림 수신 동의 여부를 수정합니다.
    """)
    @PatchMapping("/me/email-marketing-consent")
    public ApiResponse<String> updateMemberEmailMarketingConsent(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberEmailMarketingConsentUpdateRequest request
    ) {
        memberSaver.updateEmailMarketingConsent(authDetails.getMemberId(), request);
        return ApiResponse.success("이메일 혜택/이벤트 정보 알림 수신 동의 여부 수정이 완료되었습니다.");
    }

    @Operation(summary = "회원 정보 - 전화번호 혜택/이벤트 정보 알림 수신 동의 여부 수정", description = """
    ## 회원 정보 - 전화번호 혜택/이벤트 정보 알림 수신 동의 여부 수정을 수행합니다.
    - 사용자의 전화번호 혜택/이벤트 정보 알림 수신 동의 여부를 수정합니다.
    """)
    @PatchMapping("/me/sms-marketing-consent")
    public ApiResponse<String> updateMemberSmsMarketingConsent(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberSmsMarketingConsentUpdateRequest request
    ) {
        memberSaver.updateSmsMarketingConsent(authDetails.getMemberId(), request);
        return ApiResponse.success("전화번호 혜택/이벤트 정보 알림 수신 동의 여부 수정이 완료되었습니다.");
    }

    @Operation(summary = "비밀번호 인증하기", description = """
    ## 입력한 비밀번호가 현재 비밀번호와 일치하는지 확인합니다.
    - 사용자가 현재 비밀번호를 인증할 수 있습니다.
    """)
    @PostMapping("/password")
    public ApiResponse<?> certificatePassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid PasswordChangeRequest request
    ) {
        memberSaver.validateCurrentPassword(authDetails.getMemberId(), request.currentPassword());
        return ApiResponse.success("비밀번호 인증이 완료되었습니다.");
    }

    @Operation(summary = "마이페이지 주소 목록 조회", description = """
    ## 주소 목록 조회를 수행합니다.
    - 사용자가 등록한 주소 목록을 조회할 수 있습니다.
    """)
    @GetMapping("/me/addresses")
    public ApiResponse<Page<MemberAddressResponse>> getMyAddresses(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(memberFinder.findAddresses(authDetails.getMemberId(), pageable));
    }

    @Operation(summary = "주소 추가", description = """
    ## 주소 추가를 수행합니다.
    - 사용자가 주소를 추가할 수 있습니다.
    """)
    @PatchMapping("/addresses")
    public ApiResponse<?> addAddress(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid AddressRegisterRequest request
    ) {
        memberSaver.addAddress(authDetails.getMemberId(), request);
        return ApiResponse.success("주소 추가가 완료되었습니다.");
    }

    @Operation(summary = "주소 삭제", description = """
    ## 주소 삭제를 수행합니다.
    - 사용자가 주소를 삭제할 수 있습니다.
    """)
    @DeleteMapping("/addresses/{addressId}")
    public ApiResponse<?> deleteAddress(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long addressId
    ) {
        memberSaver.deleteAddress(authDetails.getMemberId(), addressId);
        return ApiResponse.success("주소 삭제가 완료되었습니다.");
    }
}