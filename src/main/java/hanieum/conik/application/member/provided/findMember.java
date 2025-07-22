package hanieum.conik.application.member.provided;

import hanieum.conik.domain.member.Member;

/**
 * 회원을 조회한다
 */
public interface findMember {
    Member find(Long memberId);
}
