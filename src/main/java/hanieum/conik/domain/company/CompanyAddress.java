package hanieum.conik.domain.company;

import hanieum.conik.domain.common.address.AddressBase;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class CompanyAddress extends AddressBase {
    protected CompanyAddress() { super(null,null,null); }

    private CompanyAddress(String postal, String street, String detail) {
        super(postal, street, detail);
    }

    public static CompanyAddress register(AddressRegisterRequest req) {
        return new CompanyAddress(
                req.addressPostalCode(),
                req.addressStreetAddress(),
                req.addressDetailAddress()
        );
    }
}
