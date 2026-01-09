package hanieum.conik.adapter.member.webapi;

import hanieum.conik.adapter.member.sms.dto.SmsAuthCodeRequest;
import hanieum.conik.adapter.member.sms.dto.SmsCertificateRequest;
import hanieum.conik.application.member.SmsCertService;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sms")
@Tag(name = "USER", description = "사용자 도메인 API")
public class SmsController {
    private final SmsCertService smsCertService;

    @Operation(summary = "휴대폰 인증번호 전송 API", description = "사용자에게 인증번호를 전송합니다.")
    @PostMapping()
    public ApiResponse<?> sendSms(@RequestBody @Valid SmsAuthCodeRequest authCodeRequest) {
        smsCertService.sendSms(authCodeRequest);
        return ApiResponse.success("문자 전송 성공");
    }

    @Operation(summary = "휴대폰 인증번호 검증 API", description = "인증번호를 검증합니다.")
    @PostMapping("/certificate")
    public ApiResponse<?> sendSmsWithCertificate(@RequestBody @Valid SmsCertificateRequest certificateRequest) {
        return ApiResponse.success(smsCertService.certificatePhoneNumber(certificateRequest));
    }
}
