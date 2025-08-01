package hanieum.conik.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberLoginResponse(
        @Schema(description = "토큰 정보")
        TokenInfo tokenInfo,

        @Schema(description = "멤버 정보")
        MemberInfo memberInfo
) { }
