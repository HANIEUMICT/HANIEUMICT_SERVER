package hanieum.conik.domain.user.service;

import hanieum.conik.domain.user.dto.request.AuthCodeRequest;
import hanieum.conik.domain.user.dto.request.CertificateRequest;

/**
 * 이메일 인증 관련 기능을 제공하는 서비스 인터페이스
 */
public interface EmailService {

    /**
     * 이메일로 인증 코드를 전송하고, 해당 코드를 Redis 등에 저장합니다.
     *
     * @param authCodeRequest 인증번호를 보낼 이메일 정보
     */
    void sendEmail(AuthCodeRequest authCodeRequest);

    /**
     * 사용자가 입력한 인증 코드를 검증합니다.
     *
     * @param certificateRequest 이메일 인증 요청 정보
     * @return 인증이 성공하면 true, 실패 시 예외 발생
     */
    Boolean certificateEmail(CertificateRequest certificateRequest);

}