package hanieum.conik.domain.member;

import hanieum.conik.adapter.member.dto.MemberSignUpRequest;
import hanieum.conik.adapter.member.persistence.EmailAttributeConverter;
import hanieum.conik.domain.member.enumerate.MemberRole;
import hanieum.conik.domain.member.exception.UserErrorType;
import hanieum.conik.domain.member.exception.UserException;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

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
    private String password;

    @Column(name = "terms_of_service_agreed", nullable = false)
    private Boolean termsOfServiceAgreed;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    private Member(Email email, String password, String phoneNumber, Boolean termsOfServiceAgreed, MemberRole role) {
        if (!termsOfServiceAgreed) {
            throw new UserException(UserErrorType.TERMS_NOT_AGREED);
        }
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.termsOfServiceAgreed = termsOfServiceAgreed;
        this.role = role;
    }

    /** 회원가입 */
    public static Member signUp(MemberSignUpRequest request) {
        return new Member(new Email(request.email()), request.password(), request.phoneNumber(), request.termsOfServiceAgreed(), request.role());
    }

    /** 로그인 등에서 비밀번호 검증 */
    public boolean verifyPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    /** 비밀번호 변경 */
    public void changePassword(String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword)) {
            throw new UserException(UserErrorType.INVALID_PASSWORD);
        }
        this.password = newPassword;
    }
}
