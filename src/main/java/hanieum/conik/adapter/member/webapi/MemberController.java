package hanieum.conik.adapter.member.webapi;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.application.member.MemberFinderService;
import hanieum.conik.application.member.MemberModifyService;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.dto.PasswordChangeRequest;
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

    private final MemberModifyService memberModifyService;
    private final MemberFinderService memberFinderService;
    private final MemberRepository memberRepository;

    @Operation(summary = "회원 정보 수정", description = """
    ## 회원 정보 수정을 수행합니다.
    - 사용자의 이름, 전화번호, 비밀번호, 이용약관 정보를 수정합니다.
    """)
    @PatchMapping("/profile")
    public ApiResponse<?> updateMemberProfile(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid MemberProfileUpdateRequest request
    ) {
        memberModifyService.updateProfile(authDetails.getMemberId(), request);
        return ApiResponse.success("회원 정보 수정이 완료되었습니다.");
    }

    @Operation(summary = "비밀번호 인증하기", description = """
    ## 입력한 비밀번호가 현재 비밀번호와 일치하는지 확인합니다.
    - 사용자가 현재 비밀번호를 인증할 수 있습니다.
    """)
    @PostMapping("/password")
    public ApiResponse<?> certificatePassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid PasswordChangeRequest request
    ){
        memberModifyService.certificatePassword(authDetails.getMemberId(), request.currentPassword());
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
        Member member = memberFinderService.findById(authDetails.getMemberId());
        return ApiResponse.success(memberFinderService.findAddresses(member.getId(), pageable));
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
        memberModifyService.deleteAddress(authDetails.getMemberId(), addressId);
        return ApiResponse.success("주소 삭제가 완료되었습니다.");
    }
}