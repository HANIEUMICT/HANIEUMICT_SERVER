package hanieum.conik.application.member.provided;

import hanieum.conik.adapter.member.dto.MemberLoginRequest;
import hanieum.conik.adapter.member.dto.MemberLoginResponse;
import hanieum.conik.adapter.member.dto.MemberSignUpRequest;
import hanieum.conik.domain.member.Member;
import jakarta.validation.Valid;

/**
 * 회원가입/로그인 로직을 구현한다.
 */
public interface Auth {
    MemberLoginResponse login(@Valid MemberLoginRequest loginRequest);
    MemberLoginResponse register(@Valid MemberSignUpRequest signUpRequest);
}
