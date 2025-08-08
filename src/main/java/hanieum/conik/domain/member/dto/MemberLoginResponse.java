package hanieum.conik.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public record MemberLoginResponse(
        @Schema(description = "토큰 정보")
        TokenInfo tokenInfo,

        @Schema(description = "멤버 정보")
        MemberInfo memberInfo,

        @Schema(description = "멤버가 속한 기업 id", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long companyId
) {
        public MemberLoginResponse {
                Objects.requireNonNull(tokenInfo, "tokenInfo 는 필수입니다.");
                Objects.requireNonNull(memberInfo, "memberInfo 는 필수입니다.");
        }

        public static MemberLoginResponse individual(TokenInfo token, MemberInfo memberInfo) {
                return new MemberLoginResponse(token, memberInfo, null);
        }
        public static MemberLoginResponse corporate(TokenInfo token, MemberInfo memberInfo, Long companyId) {
                return new MemberLoginResponse(token, memberInfo, Objects.requireNonNull(companyId));
        }
}
