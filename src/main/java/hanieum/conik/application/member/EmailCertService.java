package hanieum.conik.application.member;

import hanieum.conik.application.member.required.EmailSender;
import hanieum.conik.global.application.required.MemoryMap;
import hanieum.conik.adapter.member.email.dto.AuthCodeRequest;
import hanieum.conik.adapter.member.email.dto.CertificateRequest;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailCertService{
    private final EmailSender emailSender;
    private final MemoryMap memoryMap;

    private static final long OTP_TIMEOUT = 5 * 60_000L; // 5분

    public void sendEmail(AuthCodeRequest authCodeRequest) {
        int authNumber = emailSender.sendAuthMail(authCodeRequest.email());
        memoryMap.setValue(authCodeRequest.email(), String.valueOf(authNumber), OTP_TIMEOUT);
    }

    public Boolean certificateEmail(CertificateRequest certificateRequest) {
        if (memoryMap.getValue(certificateRequest.email()).equals(certificateRequest.authCode())) {
            memoryMap.deleteValue(certificateRequest.email());
            return true;
        }
        else{
            throw new MemberException(MemberErrorType.INVALID_AUTHORIZATION_CODE);
        }
    }
}
