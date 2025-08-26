package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CompanyAddress{
    @Column(name = "address_postal_code", length = 20, nullable = false)
    private String postalCode;
    @Column(name = "address_street_address", length = 255, nullable = false)
    private String streetAddress;
    @Column(name = "address_detail_address", length = 255)
    private String detailAddress;

    private CompanyAddress(String postalCode, String streetAddress, String detailAddress){
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }

    public static CompanyAddress register(AddressRegisterRequest req) {
        return new CompanyAddress(
                req.postalCode(),
                req.streetAddress(),
                req.detailAddress()
        );
    }

    public static CompanyAddress from(AddressRegisterRequest dto) {
        return new CompanyAddress(dto.postalCode(), dto.streetAddress(), dto.detailAddress());
    }
}
