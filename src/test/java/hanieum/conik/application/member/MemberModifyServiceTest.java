package hanieum.conik.application.member;

import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MemberModifyServiceTest {
    @Autowired
    MemberModifyService memberModifyService;

    @MockBean
    MemberFinder memberFinder;
    @MockBean
    PasswordEncoder passwordEncoder;

    /***
     * =================================================================================================================
     * updateProfile - 관련 테스트
     */

    @Test
    @DisplayName("updateProfile 실패 - 멤버 없음이면 예외 전파 및 encode/update 미호출")
    void updatePhoneNumber_memberNotFound() {
        // given
        long memberId = 999L;
        MemberProfileUpdateRequest req = mock(MemberProfileUpdateRequest.class);
        given(memberFinder.findById(memberId)).willThrow(new MemberException(MemberErrorType.MEMBER_NOT_FOUND));

        // when / then
        assertThrows(RuntimeException.class, () -> memberModifyService.updateProfile(memberId, req));

        then(passwordEncoder).should(never()).encode(anyString());
    }

    /***
     * =================================================================================================================
     * certificatePassword - 관련 테스트
     */

    @Test
    @DisplayName("certificatePassword 성공")
    void certificatePassword_success() {
        //given
        long memberId = 1L;
        String currentPassword = "currentPassword";
        Member member = mock(Member.class);
        given(memberFinder.findById(memberId)).willReturn(member);
        given(member.getHashedPassword()).willReturn("hashedPassword");
        given(passwordEncoder.matches(currentPassword, member.getHashedPassword())).willReturn(true);

        //when
        memberModifyService.certificatePassword(memberId, currentPassword);

        //then
        then(passwordEncoder).should().matches(currentPassword, member.getHashedPassword());
    }

    @Test
    @DisplayName("certificatePassword 실패")
    void certificatePassword_fail() {
        //given
        long memberId = 1L;
        String currentPassword = "currentPassword";
        String wrongPassword = "wrongPassword";
        Member member = mock(Member.class);
        given(memberFinder.findById(memberId)).willReturn(member);
        given(member.getHashedPassword()).willReturn("hashedPassword");
        given(passwordEncoder.matches(currentPassword, member.getHashedPassword())).willReturn(true);

        //when
        assertThrows(MemberException.class, () -> memberModifyService.certificatePassword(memberId, wrongPassword));
    }

    /***
     * =================================================================================================================
     * address - 관련 테스트
     */
    @Test
    @DisplayName("addAddress 성공")
    void addAddress_success() {
        //given
        long memberId = 1L;
        Member member = mock(Member.class);
        given(memberFinder.findById(memberId)).willReturn(member);

        AddressRegisterRequest addressRequest = new AddressRegisterRequest("12345", "행복로", "101호");
        ArgumentCaptor<MemberAddress> addressCaptor = ArgumentCaptor.forClass(MemberAddress.class);
        //when
        memberModifyService.addAddress(memberId, addressRequest);

        //then
        then(member).should().addAddress(addressCaptor.capture());
        MemberAddress capturedAddress = addressCaptor.getValue();

        assertEquals("12345", capturedAddress.getPostalCode());
        assertEquals("행복로", capturedAddress.getStreetAddress());
        assertEquals("101호", capturedAddress.getDetailAddress());
    }

    @Test
    @DisplayName("deleteAddress 성공")
    void deleteAddress_success() {
        // given
        long memberId = 1L;
        long addressId = 1L;

        Member member = mock(Member.class);
        given(memberFinder.findById(memberId)).willReturn(member);

        // when
        memberModifyService.deleteAddress(memberId, addressId);

        // then
        then(memberFinder).should().findById(memberId);
        then(member).should().deleteAddress(addressId);
        then(member).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("deleteAddress 실패")
    void deleteAddress_fail() {
        // given
        long memberId = 1L;
        long addressId = 1L;

        Member member = mock(Member.class);
        given(memberFinder.findById(memberId)).willReturn(member);

        //when
        doThrow(new MemberException(MemberErrorType.ADDRESS_NOT_FOUND))
                .when(member).deleteAddress(addressId);
    }
}
