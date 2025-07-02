package hanieum.conik.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CertificateRequest(
    @Email
    @NotBlank
    String email,

    @NotBlank
    String authCode
){};
