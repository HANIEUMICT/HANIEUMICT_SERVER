package hanieum.conik.domain.user.service;

import hanieum.conik.global.application.required.MemoryMap;
import hanieum.conik.user.adapter.email.dto.AuthCodeRequest;
import hanieum.conik.user.adapter.email.dto.CertificateRequest;
import hanieum.conik.user.application.EmailCertService;
import hanieum.conik.user.application.required.EmailSender;
import hanieum.conik.user.domain.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailServiceImpl 테스트")
class EmailServiceImplTest {

    @Mock private EmailSender emailClient;
    @Mock private MemoryMap memoryMap;
    @InjectMocks private EmailCertService emailService;

    private AuthCodeRequest authCodeRequest;
    private CertificateRequest certificateRequest;
    private final String testEmail = "test@example.com";
    private final String testAuthCode = "123456";
    private final int testUserAuthNumber = 123456;

    @BeforeEach
    void setUp() {
        authCodeRequest = new AuthCodeRequest(testEmail);
        certificateRequest = new CertificateRequest(testEmail, testAuthCode);
    }

    @Test
    @DisplayName("이메일 인증 코드 전송 성공")
    void sendEmail_Success() {
        //given
        given(emailClient.sendAuthMail(testEmail)).willReturn(testUserAuthNumber);

        //when
        emailService.sendEmail(authCodeRequest);

        //then
        verify(emailClient).sendAuthMail(testEmail);
        verify(memoryMap).setValue(testEmail, String.valueOf(testUserAuthNumber), 5 * 60_000L);
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 성공")
    void certificateEmail() {
        // given
        given(memoryMap.getValue(testEmail)).willReturn(testAuthCode);

        // when
        Boolean result = emailService.certificateEmail(certificateRequest);

        // then
        assertThat(result).isTrue();
        verify(memoryMap).getValue(testEmail);
        verify(memoryMap).deleteValue(testEmail);
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 실패 - 잘못된 인증 코드")
    void certificateEmail_Fail_InvalidAuthCode() {
        // given
        String wrongAuthCode = "999999";
        given(memoryMap.getValue(testEmail)).willReturn(wrongAuthCode);

        // when
        UserException exception = assertThrows(UserException.class, () -> {
            emailService.certificateEmail(new CertificateRequest(testEmail, testAuthCode));
        });

        // then
        assertThat(exception).hasMessage("유효하지 않은 인증 코드입니다.");
    }
}