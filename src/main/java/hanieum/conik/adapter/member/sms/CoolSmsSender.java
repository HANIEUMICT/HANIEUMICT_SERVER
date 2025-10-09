package hanieum.conik.adapter.member.sms;

import hanieum.conik.application.member.required.SmsSender;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
public class CoolSmsSender implements SmsSender {

    private final DefaultMessageService messageService;

    public CoolSmsSender(
            @Value("${coolsms.api-key}") String apiKey,
            @Value("${coolsms.api-secret}") String apiSecret
    ) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Value("${coolsms.sender-number}")
    private String sender;

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
     * 인증 문자를 전송하는 메서드
     *
     * @param phoneNumber 수신할 전화번호
     * @return 생성된 인증번호
     */
    @Override
    public int sendAuthSms(String phoneNumber) {
        int authNumber = generateAuthNumber();

        String message = "[CONIC] 인증번호 [" + authNumber + "]를 입력해주세요.";

        sendSms(phoneNumber, message);

        return authNumber;
    }

    /**
     * 문자를 전송하는 메서드
     *
     * @param phoneNumber 수신할 전화번호
     * @param message 발송할 내용
     */
    @Override
    public void sendSms(String phoneNumber, String message) {
        try {
            String processed = message.replace("\\n", "\n");

            Message msg = new Message();
            msg.setFrom(sender);
            msg.setTo(phoneNumber);
            msg.setText(processed);

            messageService.sendOne(new SingleMessageSendingRequest(msg));
            log.info("SMS 전송 성공: {}", phoneNumber);
        } catch (Exception e) {
            log.error("SMS 전송 실패 ({}): {}", phoneNumber, e.getMessage());
            throw new RuntimeException("SMS 전송에 실패했습니다.");
        }
    }
}
