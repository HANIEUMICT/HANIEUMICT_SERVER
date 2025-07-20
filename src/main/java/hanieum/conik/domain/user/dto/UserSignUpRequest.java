package hanieum.conik.domain.user.dto;

import hanieum.conik.domain.user.UserRole;
import jakarta.validation.constraints.Email;

public record UserSignUpRequest(@Email String email,
                                String password,
                                String phoneNumber,
                                Boolean termsOfServiceAgreed,
                                UserRole role) {}
