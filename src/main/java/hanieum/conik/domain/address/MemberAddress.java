package hanieum.conik.domain.address;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class MemberAddress extends AddressBase {

    protected MemberAddress() { super(null,null,null); }

    private MemberAddress(String postal, String street, String detail) {
        super(postal, street, detail);
    }

    /**
     * 주소를 등록한다
     */
    public static MemberAddress register(AddressRegisterRequest req) {
        return new MemberAddress(
                req.addressPostalCode(),
                req.addressStreetAddress(),
                req.addressDetailAddress()
        );
    }
}
