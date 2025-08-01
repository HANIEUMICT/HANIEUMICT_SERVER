package hanieum.conik.application.member;

import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.domain.member.dto.*;
import hanieum.conik.application.member.provided.Auth;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.application.jwt.required.JwtTokenProviderPort;
import hanieum.conik.global.application.required.MemoryMap;
import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class AuthService implements Auth, TokenRefresh {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderPort jwtTokenProviderPort;
    private final MemoryMap memoryMap;

    @Override
    public MemberLoginResponse signUpIndividual(MemberSignUpRequest request) {
        System.out.println(1);
        checkDuplicateEmail(request);
        System.out.println(2);
        Member member = Member.signUpIndividual(getHashedRequest(request));
        System.out.println(3);
        memberRepository.save(member);

        return login(MemberLoginRequest.from(request));
    }

    @Override
    public MemberLoginResponse signUpCompanyMember(MemberSignUpRequest request, Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(CompanyErrorType.COMPANY_NOT_FOUND));

        checkDuplicateEmail(request);

        Member member = Member.signUpCompanyMember(getHashedRequest(request), company.getId());

        memberRepository.save(member);

        return login(MemberLoginRequest.from(request));
    }

    @Override
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(() -> new AuthException(AuthErrorType.MEMBER_NOT_FOUND));

        if (member.verifyPassword(request.password(), passwordEncoder)) {
            String accessToken = jwtTokenProviderPort.createAccessToken(member.getId());
            String refreshToken = jwtTokenProviderPort.createRefreshToken(member.getId());

            String key = "auth:refresh:" + member.getId();
            memoryMap.setValue(key, refreshToken, jwtTokenProviderPort.getRefreshTokenExpiration());

            return new MemberLoginResponse(TokenInfo.of(accessToken, refreshToken), MemberInfo.from(member));
        } else {
            throw new MemberException(MemberErrorType.INVALID_PASSWORD);
        }
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        Long memberId = jwtTokenProviderPort.parseRefreshToken(refreshToken);

        String key = "auth:refresh:" + memberId;
        String savedToken = memoryMap.getValue(key);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
        }

        String newAccess  = jwtTokenProviderPort.createAccessToken(memberId);
        String newRefresh = jwtTokenProviderPort.createRefreshToken(memberId);

        long refreshTtl = jwtTokenProviderPort.getRefreshTokenExpiration();
        memoryMap.setValue(key, newRefresh, refreshTtl);

        long accessTtl  = jwtTokenProviderPort.getAccessTokenExpiration();

        return new TokenResponse(
                newAccess,
                newRefresh,
                accessTtl,
                refreshTtl
        );
    }

    private void checkDuplicateEmail(MemberSignUpRequest signUpRequest){
        if (memberRepository.findByEmail(new Email(signUpRequest.email())).isPresent()) {
            throw new MemberException(MemberErrorType.EMAIL_DUPLICATE);
        }
    }

    private MemberSignUpRequest getHashedRequest(MemberSignUpRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());

        return request.withHashedPassword(hashedPassword);
    }
}
