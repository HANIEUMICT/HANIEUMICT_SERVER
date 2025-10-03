package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.provided.MemberSaver;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberSaver {

    private final MemberFinder memberFinder;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        if(request.newPassword() != null && !request.newPassword().isBlank()) {
            member.updatePassword(passwordEncoder.encode(request.newPassword()));
        }

        if (request.name() != null && !request.name().isBlank()) {
            member.updateName(request.name().trim());
        }
        if (request.newPhoneNumber() != null && !request.newPhoneNumber().isBlank()) {
            member.updatePhoneNumber(request.newPhoneNumber().trim());
        }
    }

    @Override
    public void validateCurrentPassword(Long memberId, String currentPassword) {
        Member member = memberFinder.findById(memberId);

        if (!passwordEncoder.matches(currentPassword, member.getHashedPassword())) {
            throw new MemberException(MemberErrorType.INVALID_PASSWORD);
        }
    }

    @Override
    public void addAddress(Long memberId, AddressRegisterRequest request) {
        Member member = memberFinder.findById(memberId);
        MemberAddress address = MemberAddress.register(request);
        member.addAddress(address);
    }

    @Override
    public void deleteAddress(Long memberId, Long addressId) {
        Member member = memberFinder.findById(memberId);

        member.deleteAddress(addressId);
    }
}