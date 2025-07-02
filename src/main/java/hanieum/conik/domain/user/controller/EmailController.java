package hanieum.conik.domain.user.controller;

import hanieum.conik.domain.user.dto.request.AuthCodeRequest;
import hanieum.conik.domain.user.dto.request.CertificateRequest;
import hanieum.conik.domain.user.service.EmailService;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping()
    public ApiResponse<?> sendEmail(@RequestBody @Valid AuthCodeRequest authCodeRequest) {
        emailService.sendEmail(authCodeRequest);
        return ApiResponse.success("이메일 전송 성공");
    }

    @PostMapping("/certificate")
    public ApiResponse<?> sendEmailWithCertificate(@RequestBody @Valid CertificateRequest certificateRequest) {
        return ApiResponse.success(emailService.certificateEmail(certificateRequest));
    }
}
