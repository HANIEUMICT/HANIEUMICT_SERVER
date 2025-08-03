package hanieum.conik.domain.member;

import hanieum.conik.domain.address.Address;
import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.dto.MemberSignUpRequest;
import hanieum.conik.adapter.member.persistence.EmailAttributeConverter;
import hanieum.conik.domain.member.enumerate.MemberRole;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.member.shared.Email;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    @Column(name = "company_id", nullable = true)
    private Long companyId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    private List<Address> addresses = new ArrayList<>();
    
    private Member(String name, Email email, String hashedPassword, String phoneNumber, Boolean termsOfServiceAgreed, MemberRole role, Address initialAddress) {
        if (!termsOfServiceAgreed) {
            throw new MemberException(MemberErrorType.TERMS_NOT_AGREED);
        }
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.termsOfServiceAgreed = termsOfServiceAgreed;
        this.role = role;
        this.addresses.add(initialAddress);
    }

    /**
     * 개인 회원가입
     * */
    public static Member signUpIndividual(MemberSignUpRequest request) {
        Address addr = Address.register(request.addressRegisterRequest());
        return new Member(
                request.name(),
                new Email(request.email()),
                request.password(),
                request.phoneNumber(),
                request.termsOfServiceAgreed(),
                MemberRole.INDIVIDUAL,
                addr
        );
    }

    /**
     * 기업 회원가입
     * */
    public static Member signUpCompanyMember(MemberSignUpRequest request, Long companyId) {
        Address addr = Address.register(request.addressRegisterRequest());
        Member member = new Member(
                request.name(),
                new Email(request.email()),
                request.password(),
                request.phoneNumber(),
                request.termsOfServiceAgreed(),
                MemberRole.OWNER,
                addr
        );
        member.assignCompany(companyId);
        return member;
    }

    /**
     * 비밀번호 검증
     */
    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashedPassword);
    }

    /**
     * 회원 정보 수정
     */
    public void updateProfile(MemberProfileUpdateRequest request) {
        this.phoneNumber = request.newPhoneNumber();
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    /**
     * 회원 주소 추가
     */
    public void addAddress(Address address) {
        this.addresses.add(address);
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
}
