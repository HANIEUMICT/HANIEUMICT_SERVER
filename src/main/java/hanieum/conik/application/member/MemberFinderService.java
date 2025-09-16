package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.common.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
}
