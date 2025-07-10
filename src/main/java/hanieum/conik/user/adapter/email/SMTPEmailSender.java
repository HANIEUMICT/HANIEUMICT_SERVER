package hanieum.conik.user.adapter.email;

import hanieum.conik.user.application.required.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMTPEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;


    /**
     * 인증번호를 생성하는 메서드
     * 6자리의 랜덤 숫자를 생성합니다.
     *
     * @return 생성된 인증번호
     */
    @Override
    public int generateAuthNumber() {
        return (int)(Math.random() * (90000)) + 100000;
    }

    /**
     * 인증 메일을 생성하는 메서드
     *
     * @param toEmail 수신자 이메일 주소
     * @param authNumber 인증번호
     * @return 생성된 MimeMessage 객체
     * @throws MessagingException 메일 생성 중 발생할 수 있는 예외
     */
    @Override
    public MimeMessage createAuthMail(String toEmail, int authNumber) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(toEmail);
        helper.setSubject("Conic 회원가입 인증코드 발송"); // 이메일 제목

        // Thymeleaf 컨텍스트에 변수 추가
        Context context = new Context();
        context.setVariable("authCode", String.valueOf(authNumber));
        String htmlContent = templateEngine.process("email/auth-code3", context);
        helper.setText(htmlContent, true);

        return message;
    }

    /**
     * 인증 메일을 전송하는 메서드
     *
     * @param mail 이메일 내용
     * @return 생성된 인증번호
     */
    @Override
    public int sendAuthMail(String mail) {
        int authNumber = generateAuthNumber();
        try{
            MimeMessage message = createAuthMail(mail, authNumber);
            javaMailSender.send(message);
            log.info("이메일 전송 성공: {}", authNumber);
            return authNumber;
        }
        catch (Exception e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }
    }
}
