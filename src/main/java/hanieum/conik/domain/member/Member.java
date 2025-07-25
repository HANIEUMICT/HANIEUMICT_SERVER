package hanieum.conik.domain.member;

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

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private Member(Email email, String hashedPassword, String phoneNumber, Boolean termsOfServiceAgreed, MemberRole role) {
        if (!termsOfServiceAgreed) {
            throw new MemberException(MemberErrorType.TERMS_NOT_AGREED);
        }
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.termsOfServiceAgreed = termsOfServiceAgreed;
        this.role = role;
    }

    /**
     * 개인 회원가입
     * */
    public static Member signUpIndividual(MemberSignUpRequest request) {
        return new Member(new Email(request.email()), request.password(), request.phoneNumber(), request.termsOfServiceAgreed(), MemberRole.INDIVIDUAL);
    }

    /**
     * 기업 회원가입
     * */
    public static Member signUpCompanyMember(MemberSignUpRequest request, Long companyId) {
        Member member = new Member(new Email(request.email()), request.password(), request.phoneNumber(), request.termsOfServiceAgreed(), MemberRole.OWNER);
        member.assignCompany(companyId);
        return member;
    }

    /**
     * 비밀번호 검증
     * @param rawPassword
     * @param encoder
     * @return
     */
    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashedPassword);
    }

    private void assignCompany(Long companyId) {
        if (this.role != MemberRole.OWNER) {
            throw new MemberException(MemberErrorType.INVALID_ROLE_FOR_COMPANY);
        }
        this.companyId = companyId;
    }
}
