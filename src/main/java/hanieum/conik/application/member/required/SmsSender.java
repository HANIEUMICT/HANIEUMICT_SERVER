package hanieum.conik.application.member.required;

public interface SmsSender {

    /**
     * 인증번호를 생성하는 메서드
     * 6자리의 랜덤 숫자를 생성합니다.
     *
     * @return 생성된 인증번호
     */
    int generateAuthNumber();

    /**
     * 인증 문자를 전송하는 메서드
     *
     * @param phoneNumber 수신할 전화번호
     * @return 생성된 인증번호
     */
    int sendAuthSms(String phoneNumber);

    /**
     * 문자를 전송하는 메서드
     *
     * @param phoneNumber 수신할 전화번호
     * @param content 발송할 내용
     */
    void sendSms(String phoneNumber, String content);

}
