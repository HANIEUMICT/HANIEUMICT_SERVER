package hanieum.conik.user.application.required;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailSender {

    /**
     * 인증번호를 생성하는 메서드
     * 6자리의 랜덤 숫자를 생성합니다.
     *
     * @return 생성된 인증번호
     */
    int generateAuthNumber();

    /**
     * 인증 메일을 생성하는 메서드
     *
     * @param toEmail 수신자 이메일 주소
     * @param authNumber 인증번호
     * @return 생성된 MimeMessage 객체
     * @throws MessagingException 메일 생성 중 발생할 수 있는 예외
     */
    MimeMessage createAuthMail(String toEmail, int authNumber) throws MessagingException;

    /**
     * 인증 메일을 전송하는 메서드
     *
     * @param mail 이메일 내용
     * @return 생성된 인증번호
     */
    int sendAuthMail(String mail);
}
