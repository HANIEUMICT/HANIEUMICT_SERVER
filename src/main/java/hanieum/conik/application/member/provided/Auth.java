package hanieum.conik.application.member.provided;

import hanieum.conik.domain.member.dto.EmailAvailabilityRequest;
import hanieum.conik.domain.member.dto.MemberLoginRequest;
import hanieum.conik.domain.member.dto.MemberLoginResponse;
import hanieum.conik.domain.member.dto.MemberSignUpRequest;

/**
 * 회원가입/로그인 로직을 구현한다.
 */
public interface Auth {
    MemberLoginResponse login(MemberLoginRequest loginRequest);
    MemberLoginResponse signUpIndividual(MemberSignUpRequest signUpRequest);
    MemberLoginResponse signUpCompanyMember(MemberSignUpRequest signUpRequest, Long companyId);
    void checkDuplicateEmail(EmailAvailabilityRequest email);
}
