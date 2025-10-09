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
        boolean emailMarketingAgreed,
        boolean smsMarketingAgreed,
        MemberRole memberType,
        List<MemberAddressResponse> addresses
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhoneNumber(),
                member.getTermsOfServiceAgreed(),
                member.getEmailMarketingAgreed(),
                member.getSmsMarketingAgreed(),
                member.getRole(),
                member.getSortedAddresses().stream()
                        .map(address -> MemberAddressResponse.from(address, member))
                        .toList()
        );
    }
}
