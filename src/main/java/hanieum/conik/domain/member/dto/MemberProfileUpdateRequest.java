package hanieum.conik.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberProfileUpdateRequest(
        @NotBlank(message = "전화번호 입력은 필수입니다.")
        String newPhoneNumber
){}