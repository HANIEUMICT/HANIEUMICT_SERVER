package hanieum.conik.domain.member;

import hanieum.conik.adapter.member.dto.MemberSignUpRequest;
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

    /** 회원가입 */
    public static Member signUp(MemberSignUpRequest request) {
        return new Member(new Email(request.email()), request.password(), request.phoneNumber(), request.termsOfServiceAgreed(), request.role());
    }

    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashedPassword);
    }
}
