package hanieum.conik.domain.user.service;

import hanieum.conik.domain.user.dto.request.AuthCodeRequest;
import hanieum.conik.domain.user.dto.request.CertificateRequest;
import hanieum.conik.domain.user.exception.UserErrorType;
import hanieum.conik.domain.user.exception.UserException;
import hanieum.conik.global.clients.email.EmailClient;
import hanieum.conik.global.clients.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailClient emailClient;
    private final RedisClient redisClient;

    private static final long OTP_TIMEOUT = 5 * 60_000L; // 5분

    public void sendEmail(AuthCodeRequest authCodeRequest) {
        int authNumber = emailClient.sendAuthMail(authCodeRequest.email());
        redisClient.setValue(authCodeRequest.email(), String.valueOf(authNumber), OTP_TIMEOUT);
    }

    public Boolean certificateEmail(CertificateRequest certificateRequest) {
        if (redisClient.getValue(certificateRequest.email()).equals(certificateRequest.authCode())) {
            redisClient.deleteValue(certificateRequest.email());
            return true;
        }
        else{
            throw new UserException(UserErrorType.INVALID_AUTHORIZATION_CODE);
        }
    }
}
