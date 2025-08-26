package hanieum.conik.application.member.provided;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.common.email.Email;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {
    Member findById(Long memberId);
    Member findByEmail(Email email);
}
