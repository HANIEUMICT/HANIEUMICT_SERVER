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

    public Boolean certificatePhoneNumber(SmsCertificateRequest certificateRequest) {
        if (memoryMap.getValue(certificateRequest.phoneNumber()).equals(certificateRequest.authCode())) {
            memoryMap.deleteValue(certificateRequest.phoneNumber());
            return true;
        }
        else{
            throw new MemberException(MemberErrorType.INVALID_AUTHORIZATION_CODE);
        }
    }
}
