package hanieum.conik.domain.member.shared;

import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * 이메일 값 객체
 */
@Slf4j
public record Email(String address) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        log.info("Email record created with address: {}", address);
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new MemberException(MemberErrorType.INVALID_EMAIL);
        }
    }
}