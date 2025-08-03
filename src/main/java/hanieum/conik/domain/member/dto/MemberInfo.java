package hanieum.conik.domain.member.dto;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.enumerate.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfo(
        @Schema(description = "멤버 ID", example = "1")
        Long memberId,

        @Schema(description = "멤버 이름", example = "홍길동")
        String memberName,

        @Schema(description = "멤버 역할")
        MemberRole memberRole
) {
    public static MemberInfo from(Member member) {
        return new MemberInfo(
                member.getId(),
                member.getName(),
                member.getRole()
        );
    }
}