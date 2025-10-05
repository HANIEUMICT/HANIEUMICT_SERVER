package hanieum.conik.application.member.required;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.common.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("""
        select distinct m
        from Member m
        left join fetch m.addresses
        left join fetch m.defaultAddress d
        where m.id = :id
    """)
    Optional<Member> findWithAddressesById(@Param("id") Long id);
    Optional<Member> findByEmail(Email email);
}
