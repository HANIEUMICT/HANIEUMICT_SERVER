package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.Auth;
import hanieum.conik.application.member.required.EmailSender;
import hanieum.conik.domain.member.dto.EmailAvailabilityRequest;
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
    private final Auth auth;
    private final MemoryMap memoryMap;

    private static final long OTP_TIMEOUT = 5 * 60_000L; // 5분

    public void sendEmail(AuthCodeRequest authCodeRequest) {
        auth.checkDuplicateEmail(EmailAvailabilityRequest.from(authCodeRequest.email())); // TODO: 여기서 checkDuplicateEmail을 하는 이유가 뭔가요? 이미 존재하는 사용자는 이메일 인증을 할 수 없는 이유는..?

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
