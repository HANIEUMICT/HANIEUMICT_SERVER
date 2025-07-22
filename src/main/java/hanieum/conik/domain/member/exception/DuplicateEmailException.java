package hanieum.conik.domain.member.exception;

import hanieum.conik.domain.member.shared.Email;

public class DuplicateEmailException extends UserException{
    public DuplicateEmailException() {
        super(UserErrorType.EMAIL_DUPLICATE);
    }
}
