package hanieum.conik.domain.member.exception;

public class DuplicateEmailException extends MemberException {
    public DuplicateEmailException() {
        super(MemberErrorType.EMAIL_DUPLICATE);
    }
}
