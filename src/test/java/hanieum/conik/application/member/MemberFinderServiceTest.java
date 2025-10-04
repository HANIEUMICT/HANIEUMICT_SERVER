package hanieum.conik.application.member;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.member.dto.MemberSignUpRequest;
import hanieum.conik.domain.member.exception.MemberException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberFinderServiceTest {
    @Autowired MemberFinderService memberFinder;
    @Autowired MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("멤버 주소 목록 조회 - 성공")
    void findAddresses_success() {
        var baseAddr = new AddressRegisterRequest("우리집", "홍길동", "010-4130-1951", "12345", "행복로", "101호", true);
        var signUpReq = new MemberSignUpRequest(
                "홍길동", "hong@example.com", "raw-pw", "010-1234-5678",
                true, baseAddr
        );

        Member member = Member.signUpIndividual(signUpReq);

        member.getAddresses().clear();
        member.setDefaultAddress(null);

        member.addAddress(MemberAddress.register(
                new AddressRegisterRequest("우리집", "홍길동", "010-4130-1951","12345", "행복로", "101호", false)));
        member.addAddress(MemberAddress.register(
                new AddressRegisterRequest("우리집", "홍길동", "010-4130-1951","12345", "행복로", "102호", false)));

        memberRepository.saveAndFlush(member);

        member.setDefaultAddress(member.getAddresses().get(0));
        memberRepository.saveAndFlush(member);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        Page<MemberAddressResponse> page = memberFinder.findAddresses(member.getId(), pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
    }


    @Test
    @DisplayName("멤버 주소 목록 조회 - 멤버 없음")
    void findAddresses_memberNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() -> memberFinder.findAddresses(999_999L, pageable))
                .isInstanceOf(MemberException.class);
    }
}