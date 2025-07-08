package hanieum.conik.domain.user.controller;

import hanieum.conik.domain.user.dto.request.AuthCodeRequest;
import hanieum.conik.domain.user.dto.request.CertificateRequest;
import hanieum.conik.domain.user.service.EmailServiceImpl;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailController {
    private final EmailServiceImpl emailServiceImpl;

    @Operation(summary = "인증번호 전송 API", description = "사용자에게 인증번호를 전송합니다.")
    @PostMapping()
    public ApiResponse<?> sendEmail(@RequestBody @Valid AuthCodeRequest authCodeRequest) {
        emailServiceImpl.sendEmail(authCodeRequest);
        return ApiResponse.success("이메일 전송 성공");
    }

    @Operation(summary = "인증번호 검증 API", description = "인증번호를 검증합니다.")
    @PostMapping("/certificate")
    public ApiResponse<?> sendEmailWithCertificate(@RequestBody @Valid CertificateRequest certificateRequest) {
        return ApiResponse.success(emailServiceImpl.certificateEmail(certificateRequest));
    }
}
