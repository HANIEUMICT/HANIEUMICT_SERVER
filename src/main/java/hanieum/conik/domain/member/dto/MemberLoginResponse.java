package hanieum.conik.domain.member.dto;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken,
        Long memberId
) { }
