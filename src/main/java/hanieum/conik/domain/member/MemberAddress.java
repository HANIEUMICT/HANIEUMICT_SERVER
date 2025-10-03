package hanieum.conik.domain.member;

import hanieum.conik.domain.common.address.AddressBase;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
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

    protected MemberAddress() { super(null,null,null); }

    private MemberAddress(String postal, String street, String detail) {
        super(postal, street, detail);
    }

    /**
     * 주소를 등록한다
     */
    public static MemberAddress register(AddressRegisterRequest req) {
        return new MemberAddress(
                req.postalCode(),
                req.streetAddress(),
                req.detailAddress()
        );
    }

    void setMember(Member member) {
        this.member = member;
    }
}
