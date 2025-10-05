package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CompanyAddress{
    @Column(name = "postal_code", length = 20, nullable = false)
    private String postalCode;

    @Column(name = "street_address", length = 255, nullable = false)
    private String streetAddress;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @Column(name = "address_name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String recipient;

    @Column(name = "address_phone_number", nullable = false, length = 20)
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    private CompanyAddress(String postalCode, String streetAddress, String detailAddress, String name, String recipient, String phoneNumber) {
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.name = name;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
    }

    public static CompanyAddress register(AddressRegisterRequest req) {
        return new CompanyAddress(
                req.postalCode(),
                req.streetAddress(),
                req.detailAddress(),
                req.addressName(),
                req.recipient(),
                req.phoneNumber()
        );
    }

    public static CompanyAddress from(AddressRegisterRequest dto) {
        return new CompanyAddress(dto.postalCode(), dto.streetAddress(), dto.detailAddress(), dto.addressName(), dto.recipient(), dto.phoneNumber());
    }

    public void update(AddressRegisterRequest dto) {
        if(dto.postalCode() != null) this.postalCode = dto.postalCode();
        if (dto.streetAddress() != null) this.streetAddress = dto.streetAddress();
        if (dto.detailAddress() != null) this.detailAddress = dto.detailAddress();
        if (dto.addressName() != null) this.name = dto.addressName();
        if (dto.recipient() != null) this.recipient = dto.recipient();
        if (dto.phoneNumber() != null) this.phoneNumber = dto.phoneNumber();
    }
}
