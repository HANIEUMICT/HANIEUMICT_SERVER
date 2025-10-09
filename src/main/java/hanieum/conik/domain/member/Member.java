package hanieum.conik.domain.member;

import hanieum.conik.adapter.member.persistence.EmailAttributeConverter;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.domain.member.dto.MemberSignUpRequest;
import hanieum.conik.domain.member.enumerate.MemberRole;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @NaturalId
    @Convert(converter = EmailAttributeConverter.class)
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private Email email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String hashedPassword;

    @Column(name = "terms_of_service_agreed", nullable = false)
    private Boolean termsOfServiceAgreed;

    // TODO : k8s 연결 후 RDS 기본 값 설정, 이후 nullable = false로 변경
    @Column(name = "email_marketing_agreed", nullable = true)
    private Boolean emailMarketingAgreed;

    @Column(name = "sms_marketing_agreed", nullable = true)
    private Boolean smsMarketingAgreed;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    @Column(name = "company_id", nullable = true)
    private Long companyId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberAddress> addresses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) // 기본 배송지 FK
    @JoinColumn(name = "default_member_address_id")
    private MemberAddress defaultAddress;

    /* ========= 생성/팩토리 ========= */

    private Member(String name, Email email, String hashedPassword, String phoneNumber, Boolean termsOfServiceAgreed, MemberRole role, MemberAddress memberAddress) {
        if (!termsOfServiceAgreed) {
            throw new MemberException(MemberErrorType.TERMS_NOT_AGREED);
        }
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.termsOfServiceAgreed = termsOfServiceAgreed;
        this.emailMarketingAgreed = false;
        this.smsMarketingAgreed = false;
        this.role = role;
        if (memberAddress != null) {
            addAddress(memberAddress);
        }
    }

    /**
     * 개인 회원가입
     * */
    public static Member signUpIndividual(MemberSignUpRequest request) {
        MemberAddress address = MemberAddress.register(request.addressRegisterRequest());

        return new Member(
                request.name(),
                new Email(request.email()),
                request.password(),
                request.phoneNumber(),
                request.termsOfServiceAgreed(),
                MemberRole.INDIVIDUAL,
                address
        );
    }

    /**
     * 기업 회원 회원가입
     * */
    public static Member signUpCompanyMember(MemberSignUpRequest request, Long companyId) {
        MemberAddress address = MemberAddress.register(request.addressRegisterRequest());
        Member member = new Member(
                request.name(),
                new Email(request.email()),
                request.password(),
                request.phoneNumber(),
                request.termsOfServiceAgreed(),
                MemberRole.OWNER,
                address
        );
        member.assignCompany(companyId);
        return member;
    }

    /* ========= 도메인 로직 ========= */

    /**
     * 비밀번호 검증
     * @param rawPassword
     * @param encoder
     * @return
     */
    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashedPassword);
    }

    /**
     * 회원 전화번호 수정
     */
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 회원 이름 수정
     */
    public void updateName(String name){
        this.name = name;
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    /**
     * 이메일 마케팅 수신동의 여부 수정
     */
    public void updateEmailMarketingAgreed(boolean emailMarketingAgreed) {
        this.emailMarketingAgreed = emailMarketingAgreed;
    }

    /**
     * 전화번호 마케팅 수신동의 여부 수정
     */
    public void updateSmsMarketingAgreed(boolean smsMarketingAgreed) {
        this.smsMarketingAgreed = smsMarketingAgreed;
    }

    /**
     * 회원 주소 추가
     */
    public void addAddress(MemberAddress address) {
        addresses.add(address);
        address.setMember(this);

        if (this.defaultAddress == null) {
            this.defaultAddress = address;
        }
    }

    /**
     * 주소 추가 + 기본 배송지로 설정 여부까지 한 번에 처리 (체크박스용)
     */
    public MemberAddress addAddress(MemberAddress address, boolean setAsDefault) {
        addAddress(address);
        if (setAsDefault) {
            setDefaultAddress(address);
        }
        return address;
    }

    /**
     * 회원 주소 수정
     */
    public void updateAddress(Long addressId, MemberAddress updatedAddress, boolean setAsDefault) {
        MemberAddress target = addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorType.ADDRESS_NOT_FOUND));

        target.update(updatedAddress);

        boolean wasDefault = (defaultAddress != null && defaultAddress.equals(target));
        if (wasDefault && !setAsDefault) {
            throw new MemberException(MemberErrorType.CANNOT_UNSET_DEFAULT_ADDRESS);
        }

        if (setAsDefault) {
            setDefaultAddress(target);
        }
    }

    /**
     * 회원 주소 삭제
     */
    public void deleteAddress(Long addressId) {
        MemberAddress target = addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorType.ADDRESS_NOT_FOUND));

        boolean wasDefault = (defaultAddress != null && defaultAddress.equals(target));

        addresses.remove(target);
        target.setMember(null);

        if (wasDefault) {
            this.defaultAddress = addresses.isEmpty() ? null : addresses.get(0);
        }
    }

    /**
     * 기업 회원인지
     */
    public boolean isCompanyMember() {

        return this.role == MemberRole.OWNER || this.role == MemberRole.STAFF;
    }

    /**
     * 기업 회원의 경우 기업을 할당한다.
     */
    private void assignCompany(Long companyId) {
        if (this.role != MemberRole.OWNER) {
            throw new MemberException(MemberErrorType.INVALID_ROLE_FOR_COMPANY);
        }
        this.companyId = companyId;
    }

    /**
     * 기본 배송지 변경(엔티티로)
     * - 반드시 나의 주소여야 한다.
     */
    public void setDefaultAddress(MemberAddress address) {
        if (address == null) {
            this.defaultAddress = null;
            return;
        }
        if (address.getMember() != this) {
            throw new MemberException(MemberErrorType.ADDRESS_NOT_FOUND);
        }
        this.defaultAddress = address;
    }
}
