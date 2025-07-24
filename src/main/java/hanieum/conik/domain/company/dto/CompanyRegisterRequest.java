package hanieum.conik.domain.company.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompanyRegisterRequest(
    @NotBlank(message = "기업 이름은 필수 입력입니다.")
    String name,

    @NotBlank(message = "기업 대표 이름은 필수 입력입니다.")
    String owner,

    @Email
    @NotBlank(message = "기업 이메일은 필수 입력입니다.")
    String email,

    @NotBlank(message = "기업 전화번호는 필수 입력입니다.")
    String phoneNumber,

    @NotBlank(message = "기업 업태명은 필수 입력입니다.")
    String businessType,

    @NotBlank(message = "기업 종목은 필수 입력입니다.")
    String industry,

    @NotBlank(message = "사업자등록 번호는 필수 입력입니다.")
    String registrationNumber,

    @NotBlank(message = "사업자등록증은 필수 입력입니다.")
    String registrationCertificateUrl,

    @NotBlank(message = "통장 사본은 필수 입력입니다.")
    String bankbookCopy,

    @NotBlank(message = "회사 소개서는 필수 입력입니다.")
    String profileUrl
) {}
