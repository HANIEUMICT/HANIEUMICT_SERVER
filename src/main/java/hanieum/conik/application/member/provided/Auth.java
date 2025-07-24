package hanieum.conik.application.member.provided;

import hanieum.conik.domain.member.dto.MemberLoginRequest;
import hanieum.conik.domain.member.dto.MemberLoginResponse;
import hanieum.conik.domain.member.dto.MemberSignUpRequest;
import jakarta.validation.Valid;

/**
 * 회원가입/로그인 로직을 구현한다.
 */
public interface Auth {
    MemberLoginResponse login(@Valid MemberLoginRequest loginRequest);
    MemberLoginResponse signUpIndividual(@Valid MemberSignUpRequest signUpRequest);
    MemberLoginResponse signUpCompanyMember(@Valid MemberSignUpRequest signUpRequest, Long companyId);
}
