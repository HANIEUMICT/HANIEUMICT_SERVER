package hanieum.conik.domain.company.dto;

import java.time.LocalDate;

public record CompanyDetailRequest(
    LocalDate establishedAt,
    String logoUrl,
    Integer employeeCount,
    String websiteUrl,
    String contactAvailableTime,
    String description
) {}
