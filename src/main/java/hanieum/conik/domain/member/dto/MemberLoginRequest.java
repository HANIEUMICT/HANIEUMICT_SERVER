package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @Schema(description = "이메일", example = "kjeng7897@gmail.com")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "비밀번호", example = "7897")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
        public static MemberLoginRequest from(MemberSignUpRequest signUpRequest) {
                return new MemberLoginRequest(
                        signUpRequest.email(),
                        signUpRequest.password()
                );
        }
}
