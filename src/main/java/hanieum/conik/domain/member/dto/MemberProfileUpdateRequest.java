package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberProfileUpdateRequest(
        @Schema(description = "이름", example = "윤도운")
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String newPhoneNumber,

        @Schema(description = "새 비밀번호", example = "day6")
        String newPassword
){}