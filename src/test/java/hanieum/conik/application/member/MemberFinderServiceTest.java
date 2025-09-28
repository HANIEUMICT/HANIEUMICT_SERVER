package hanieum.conik.application.member;

import hanieum.conik.adapter.member.dto.MemberAddressResponse;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.member.exception.MemberException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class MemberFinderServiceTest {
    @Autowired
    MemberFinder memberFinder;

    @MockBean
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버_주소목록_찾기_success")
    void 멤버_주소목록_찾기_success() {
        // given
        long memberId = 1L;

        // Member, Address 들을 목으로 준비 (서비스는 읽기만 하므로 목으로 충분)
        Member member = mock(Member.class);

        MemberAddress addr1 = mock(MemberAddress.class);
        given(addr1.getAddressPostalCode()).willReturn("12345");
        given(addr1.getAddressStreetAddress()).willReturn("행복로");
        given(addr1.getAddressDetailAddress()).willReturn("101호");

        MemberAddress addr2 = mock(MemberAddress.class);
        given(addr2.getAddressPostalCode()).willReturn("23456");
        given(addr2.getAddressStreetAddress()).willReturn("희망로");
        given(addr2.getAddressDetailAddress()).willReturn("202호");

        given(member.getAddresses()).willReturn(List.of(addr1, addr2));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

        // when
        Page<MemberAddressResponse> page = memberFinder.findAddresses(memberId, pageable);

        // then
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());

        var postalCodes = page.map(MemberAddressResponse::zipcode).getContent();
        assertTrue(postalCodes.containsAll(List.of("12345", "23456")));

        then(memberRepository).should().findById(memberId);
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("멤버_주소목록_찾기_fail - 멤버 없음")
    void 멤버_주소목록_찾기_fail_memberNotFound() {
        // given
        long memberId = 999L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThrows(MemberException.class,
                () -> memberFinder.findAddresses(memberId, Pageable.unpaged()));
        then(memberRepository).should().findById(memberId);
    }
}