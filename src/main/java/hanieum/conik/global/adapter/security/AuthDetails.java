package hanieum.conik.global.adapter.security;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.enumerate.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AuthDetails implements UserDetails {

    private final Member member;

    public AuthDetails(Member member) {
        this.member = member;
    }

    public Long getMemberId() { return member.getId(); }

    public Long getCompanyId() { return member.getCompanyId(); }

    public boolean isCompanyMember() { return member.isCompanyMember(); }

    public MemberRole getRole() { return member.getRole(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return String.valueOf(member.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
