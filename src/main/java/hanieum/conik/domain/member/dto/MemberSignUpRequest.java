package hanieum.conik.domain.member.dto;

import hanieum.conik.domain.member.enumerate.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberSignUpRequest(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password,

        @NotBlank(message = "전화번호는 필수 입력입니다.")
        String phoneNumber,

        @NotNull(message = "약관 동의 여부를 선택해주세요.")
        Boolean termsOfServiceAgreed
) {}
