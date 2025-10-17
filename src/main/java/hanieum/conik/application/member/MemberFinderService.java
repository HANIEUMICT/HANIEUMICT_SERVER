package hanieum.conik.application.member;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.adapter.member.dto.MemberInfoResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.required.MemberAddressRepository;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberFinderService implements MemberFinder {
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;

    @Override
    public Member findById(Long memberId) {
        if (memberId == null) {
            throw new GlobalException(GlobalErrorType.UNAUTHORIZED);
        }
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findByEmail(Email email) {
        if(email == null){
            throw new GlobalException(GlobalErrorType.UNAUTHORIZED);
        }
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
    }

    @Override
    public Page<MemberAddressResponse> findAddresses(Long memberId, Pageable pageable) {
       memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));

       Page<MemberAddress> memberAddresses = memberAddressRepository.findAllByMemberId(memberId, pageable);

       return memberAddresses.map(MemberAddressResponse::from);
    }

    @Override
    public Long findCompanyIdByMemberId(Long memberId) {
        Member member = findById(memberId);

        if(member.getCompanyId() == null){
            throw new MemberException(MemberErrorType.COMPANY_NOT_FOUND);
        }

        return member.getCompanyId();
    }

    @Override
    public MemberAddressResponse findAddress(Long memberId, Long addressId) {
        MemberAddress memberAddress = memberAddressRepository.findByIdAndMemberId(addressId, memberId)
                .orElseThrow(() -> new MemberException(MemberErrorType.ADDRESS_NOT_FOUND));

        return MemberAddressResponse.from(memberAddress, memberAddress.getMember());
    }

    @Override
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findWithAddressesById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));

        return MemberInfoResponse.from(member);
    }
}
