package hanieum.conik.application.member;

import hanieum.conik.adapter.member.dto.MemberLoginResponse;
import hanieum.conik.adapter.member.email.dto.CertificateRequest;
import hanieum.conik.application.member.provided.Auth;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import hanieum.conik.adapter.member.dto.MemberLoginRequest;
import hanieum.conik.adapter.member.dto.MemberSignUpRequest;
import hanieum.conik.domain.member.exception.DuplicateEmailException;
import hanieum.conik.domain.member.exception.UserErrorType;
import hanieum.conik.domain.member.exception.VerifyEmailException;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.application.jwt.required.JwtTokenProviderPort;
import hanieum.conik.global.application.jwt.required.RefreshTokenPort;
import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class AuthService implements Auth {

    private final MemberRepository memberRepository;
    private final JwtTokenProviderPort jwtTokenProviderPort;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    public MemberLoginResponse register(MemberSignUpRequest request) {

        checkDuplicateEmail(request);

        Member member = Member.signUp(new Email(request.email()), request.password(), request.phoneNumber(), request.termsOfServiceAgreed(), request.role());

        memberRepository.save(member);

        return login(new MemberLoginRequest(member.getEmail().address(), member.getPassword()));
    }

    @Override
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(() -> new AuthException(AuthErrorType.MEMBER_NOT_FOUND));

        if (member.getPassword().equals(request.password())) {
            String accessToken = jwtTokenProviderPort.createAccessToken(member.getId());
            String refreshToken = jwtTokenProviderPort.createRefreshToken(member.getId());

            refreshTokenPort.saveRefreshToken(member.getId(), refreshToken, jwtTokenProviderPort.getRefreshTokenExpiration());
            return new MemberLoginResponse(accessToken, refreshToken, member.getId());
        } else {
            throw new AuthException(UserErrorType.INVALID_PASSWORD);
        }
    }

    private void checkDuplicateEmail(MemberSignUpRequest signUpRequest){
        if (memberRepository.findByEmail(new Email(signUpRequest.email())).isPresent()) {
            throw new DuplicateEmailException();
        }
    }
}
