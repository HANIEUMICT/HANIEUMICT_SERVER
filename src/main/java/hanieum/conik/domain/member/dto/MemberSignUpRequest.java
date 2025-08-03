package hanieum.conik.domain.member.dto;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.enumerate.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberSignUpRequest(
        @Schema(description = "사용자 이름", example = "정성호")
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "이메일", example = "kjeng7897@gmail.com")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "비밀번호", example = "7897")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @NotBlank(message = "전화번호는 필수입니다.")
        String phoneNumber,

        @Schema(description = "서비스 이용약관 동의 여부")
        @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
        Boolean termsOfServiceAgreed,

        @NotNull(message = "주소는 필수 입력입니다.")
        AddressRegisterRequest addressRegisterRequest
) {
        public MemberSignUpRequest withHashedPassword(String hashedPassword) {
                return new MemberSignUpRequest(
                        this.name,
                        this.email,
                        hashedPassword,
                        this.phoneNumber,
                        this.termsOfServiceAgreed,
                        this.addressRegisterRequest
                );
        }
}
