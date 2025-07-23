package hanieum.conik.application.member.required;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.shared.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);
}
