package hanieum.conik.domain.common.address;

import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressBase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_name",nullable = false, length = 50)
    private String addressName;

    @Column(nullable = false, length = 50)
    private String recipient;

    @Column(nullable = false, length = 20)
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String detailAddress;

    protected AddressBase(String postalCode, String streetAddress, String detailAddress, String addressName, String recipient, String phoneNumber) {
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.addressName = addressName;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
    }

    protected void updateAddressBase(AddressBase addressBase) {
        this.postalCode = addressBase.getPostalCode();
        this.streetAddress = addressBase.getStreetAddress();
        this.detailAddress = addressBase.getDetailAddress();
        this.addressName = addressBase.getAddressName();
        this.recipient = addressBase.getRecipient();
        this.phoneNumber = addressBase.getPhoneNumber();
    }
}
