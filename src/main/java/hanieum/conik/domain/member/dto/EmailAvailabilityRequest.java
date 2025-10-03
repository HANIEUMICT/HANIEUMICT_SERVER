package hanieum.conik.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailAvailabilityRequest(
       @NotBlank(message = "이메일은 필수입니다.")
       @Email(message = "이메일 형식이 아닙니다.")
       String email
) {
    public static EmailAvailabilityRequest from(String email) {
        return new EmailAvailabilityRequest(email);
    }
}

