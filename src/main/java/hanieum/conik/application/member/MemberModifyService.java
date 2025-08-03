package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.MemberSaver;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.address.Address;
import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.dto.PasswordChangeRequest;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberSaver {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;  // 비밀번호 검증용

    @Override
    public void updateProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
        member.updateProfile(request);
    }

    @Override
    public void updatePassword(Long memberId, PasswordChangeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), member.getHashedPassword())) {
            throw new MemberException(MemberErrorType.INVALID_PASSWORD);
        }

        String newHashed = passwordEncoder.encode(request.newPassword());
        member.updatePassword(newHashed);
    }

    @Override
    public void addAddress(Long memberId, AddressRegisterRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
        Address address = Address.register(request);
        member.addAddress(address);
    }
}