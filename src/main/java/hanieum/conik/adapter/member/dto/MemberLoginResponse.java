package hanieum.conik.adapter.member.dto;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken,
        Long memberId
) { }
