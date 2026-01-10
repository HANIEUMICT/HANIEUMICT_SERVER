package hanieum.conik.application.member.provided;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.adapter.member.dto.MemberInfoResponse;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.common.email.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {
    Member findById(Long memberId);
    Member findByEmail(Email email);
    Page<MemberAddressResponse> findAddresses(Long memberId, Pageable pageable);
    Long findCompanyIdByMemberId(Long memberId);
    MemberAddressResponse findAddress(Long memberId, Long addressId);
    MemberInfoResponse getMemberInfo(Long memberId);
    List<Member> findAllByIds(List<Long> memberIds);
}
