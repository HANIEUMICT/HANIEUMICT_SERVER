package hanieum.conik.application.member;

import hanieum.conik.adapter.member.sms.dto.SmsAuthCodeRequest;
import hanieum.conik.adapter.member.sms.dto.SmsCertificateRequest;
import hanieum.conik.application.member.required.SmsSender;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.global.application.required.MemoryMap;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsCertService {
    private final SmsSender smsSender;
    private final MemoryMap memoryMap;

    private static final long OTP_TIMEOUT = 5 * 60_000L; // 5분

    public void sendSms(SmsAuthCodeRequest authCodeRequest) {
        int authNumber = smsSender.sendAuthSms(authCodeRequest.phoneNumber());
        memoryMap.setValue(authCodeRequest.phoneNumber(), String.valueOf(authNumber), OTP_TIMEOUT);
    }

    // TODO : 논의 후 Sms,EmailCertService에서 MemoryMap 직접 접근 -> VerificationStore 포트로 추상화 (현재 PhoneVerificationStore만 존재)
    public Boolean certificatePhoneNumber(SmsCertificateRequest certificateRequest) {
        String phoneNumber = certificateRequest.phoneNumber();
        String inputCode = certificateRequest.authCode();

        String storedCode = memoryMap.getValue(phoneNumber);
        if (storedCode == null) {
            throw new MemberException(MemberErrorType.EXPIRED_VERIFICATION_CODE);
        }
        if (!storedCode.equals(inputCode)) {
            throw new MemberException(MemberErrorType.INVALID_AUTHORIZATION_CODE);
        }

        memoryMap.setValue("verified:" + phoneNumber, "true", OTP_TIMEOUT);
        memoryMap.deleteValue(phoneNumber);

        return true;
    }
}
