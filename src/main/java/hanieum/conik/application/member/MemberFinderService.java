package hanieum.conik.application.member;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.common.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberFinderService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findByEmail(Email email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
    }

    @Override
    public Page<MemberAddressResponse> findAddresses(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
        List<MemberAddress> addresses = member.getAddresses();

        Page<MemberAddress> addressPage = PageableExecutionUtils.getPage(
                addresses,
                pageable,
                addresses::size
        );

        return addressPage.map(MemberAddressResponse::from);
    }
}
