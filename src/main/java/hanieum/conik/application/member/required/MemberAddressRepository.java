package hanieum.conik.application.member.required;

import hanieum.conik.domain.member.MemberAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    Page<MemberAddress> findAllByMemberId(Long memberId, Pageable pageable);
    Optional<MemberAddress> findByIdAndMemberId(Long addressId, Long memberId);
}
