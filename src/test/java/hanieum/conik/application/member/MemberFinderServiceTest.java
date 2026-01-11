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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MemberFinderServiceTest {
    @InjectMocks MemberFinderService memberFinder;
    @Mock MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 주소 목록 조회 - 멤버 없음")
    void findAddresses_memberNotFound() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        // findById 등 내부에서 사용하는 메서드가 있다면 stubbing 필요
        // 예: given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberFinder.findAddresses(999_999L, pageable))
                .isInstanceOf(MemberException.class);
    }
}