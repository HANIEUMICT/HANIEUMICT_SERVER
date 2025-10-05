package hanieum.conik.domain.member;

import hanieum.conik.domain.common.address.AddressBase;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.common.address.exception.AddressErrorType;
import hanieum.conik.domain.common.address.exception.AddressException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class MemberAddress extends AddressBase {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected MemberAddress() { super(null,null,null,null,null,null); }

    private MemberAddress(String postalCode, String streetAddress, String detailAddress, String addressName, String recipient, String phoneNumber) {
        super(postalCode, streetAddress, detailAddress, addressName, recipient, phoneNumber);
    }

    /**
     * 주소 정보를 업데이트 한다.
     */
    public static MemberAddress register(AddressRegisterRequest request) {
        if (request.addressName() == null || request.addressName().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_NAME);
        }
        if (request.recipient() == null || request.recipient().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_RECIPIENT);
        }
        if (request.phoneNumber() == null || request.phoneNumber().isBlank()) {
            if (!request.phoneNumber().matches("^010-\\d{4}-\\d{4}$")) {
                throw new AddressException(AddressErrorType.INVALID_PHONE_NUMBER);
            }
            throw new AddressException(AddressErrorType.INVALID_PHONE_NUMBER);
        }
        if (request.postalCode() == null || request.postalCode().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_POSTAL_CODE);
        }
        if (request.streetAddress() == null || request.streetAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_STREET_ADDRESS);
        }
        if (request.detailAddress() == null || request.detailAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_DETAIL_ADDRESS);
        }
        return new MemberAddress(
                request.postalCode().trim(),
                request.streetAddress().trim(),
                request.detailAddress().trim(),
                request.addressName().trim(),
                request.recipient().trim(),
                request.phoneNumber().trim()
        );
    }

    void setMember(Member member) {
        this.member = member;
    }
}
