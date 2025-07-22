package hanieum.conik.domain.member.shared;

import hanieum.conik.domain.member.exception.UserErrorType;
import hanieum.conik.domain.member.exception.UserException;

import java.util.regex.Pattern;

/**
 * 이메일 값 객체
 */
public record Email(String address) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new UserException(UserErrorType.INVALID_EMAIL);
        }
    }
}