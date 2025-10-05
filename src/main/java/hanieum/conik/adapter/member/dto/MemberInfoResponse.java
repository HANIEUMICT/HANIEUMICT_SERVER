package hanieum.conik.adapter.member.dto;

import hanieum.conik.domain.common.email.Email;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.enumerate.MemberRole;

import java.util.List;

public record MemberInfoResponse(
        Long id,
        Email email,
        String name,
        String phoneNumber,
        boolean termsOfServiceAgreed,
        boolean emailConsent,
        boolean smsConsent,
        MemberRole memberType,
        List<MemberAddressResponse> addresses,
        MemberAddressResponse defaultAddress
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhoneNumber(),
                member.getTermsOfServiceAgreed(),
                member.getEmailConsent(),
                member.getSmsConsent(),
                member.getRole(),
                member.getAddresses().stream()
                        .map(MemberAddressResponse::from)
                        .toList(),
                MemberAddressResponse.from(member.getDefaultAddress())
        );
    }
}
