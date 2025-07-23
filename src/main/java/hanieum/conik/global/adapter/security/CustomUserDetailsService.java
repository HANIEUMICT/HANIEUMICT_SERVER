package hanieum.conik.global.adapter.security;

import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long memberId = Long.valueOf(username);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        return new CustomUserDetails(member);
    }
}
