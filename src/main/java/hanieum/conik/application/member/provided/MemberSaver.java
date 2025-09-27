package hanieum.conik.application.member.provided;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.dto.MemberProfileUpdateRequest;

/**
 * 회원정보 변경/저장을 담당하는 포트 인터페이스
 */
public interface MemberSaver {
    /**
     * 회원가입 시 입력한 기본 정보(이메일, 전화번호 등)를 수정합니다.
     *
     * @param memberId    수정할 멤버의 id
     * @param updateReq   수정할 필드를 담은 DTO
     */
    void updateProfile(Long memberId, MemberProfileUpdateRequest updateReq);

    void certificatePassword(Long memberId, String currentPassword);

    /**
     * 회원에게 새 주소를 추가합니다.
     *
     * @param memberId    수정할 멤버의 id
     * @param addressRegister   새로운 주소 정보를 담은 DTO
     */
    void addAddress(Long memberId, AddressRegisterRequest addressRegister);

    /**
     * 회원의 주소를 삭제합니다.
     *
     * @param memberId    수정할 멤버의 id
     * @param addressId   삭제할 주소의 id
     */
    void deleteAddress(Long memberId, Long addressId);
}