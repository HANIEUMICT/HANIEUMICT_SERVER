package hanieum.conik.domain.company;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends AbstractEntity {

    private String name;

    private String owner;

    private String ownerEmail;

    private String ownerPhoneNumber;

    private String businessType;

    private String industry;

    private String RegistrationNumber;

    private String registrationCertificateUrl;

    private String bankAccountUrl;

    private String profileUrl;

    private CompanyStatus status;
}