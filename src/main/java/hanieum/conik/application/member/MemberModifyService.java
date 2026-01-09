package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.provided.MemberSaver;
import hanieum.conik.application.member.required.PhoneVerificationStore;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.dto.*;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final PhoneVerificationStore phoneVerificationStore;

    @Override
    public void updateProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        boolean hasCurrentPassword = StringUtils.isNotBlank(request.currentPassword());
        boolean hasNewPassword = StringUtils.isNotBlank(request.newPassword());

        if (hasCurrentPassword != hasNewPassword) {
            throw new MemberException(MemberErrorType.INVALID_PASSWORD_UPDATE_REQUEST);
        }
        if (hasNewPassword) {
            validateCurrentPassword(memberId, request.currentPassword());
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
    public void updateName(Long memberId, MemberNameUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        member.updateName(request.name().trim());
    }

    @Override
    public void updatePassword(Long memberId, MemberPasswordUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        validateCurrentPassword(memberId, request.currentPassword());
        member.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Override
    public void updatePhoneNumber(Long memberId, MemberPhoneNumberUpdateRequest request) {
        Member member = memberFinder.findById(memberId);
        String newPhoneNumber = request.newPhoneNumber().trim();

        if (!phoneVerificationStore.isVerified(newPhoneNumber)) {
            throw new MemberException(MemberErrorType.INVALID_PHONE_NUMBER);
        }

        member.updatePhoneNumber(newPhoneNumber);

        phoneVerificationStore.removeVerifiedFlag(newPhoneNumber);
    }

    @Override
    public void updateEmailMarketingConsent(Long memberId, MemberEmailMarketingConsentUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        member.updateEmailMarketingAgreed(request.isEmailMarketingAgreed());
    }

    @Override
    public void updateSmsMarketingConsent(Long memberId, MemberSmsMarketingConsentUpdateRequest request) {
        Member member = memberFinder.findById(memberId);

        member.updateSmsMarketingAgreed(request.isSmsMarketingAgreed());
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
        member.addAddress(address, request.isDefault());
    }

    @Override
    public void updateAddress(Long memberId, Long addressId, AddressRegisterRequest request) {
        Member member = memberFinder.findById(memberId);
        MemberAddress address = MemberAddress.register(request);
        member.updateAddress(addressId, address, request.isDefault());
    }

    @Override
    public void deleteAddress(Long memberId, Long addressId) {
        Member member = memberFinder.findById(memberId);

        member.deleteAddress(addressId);
    }
}