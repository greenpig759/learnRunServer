package com.example.learnRunServer.token;

import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String kakaoId;
    private final Collection<? extends GrantedAuthority> authorities; // final로 필수 초기화

    public CustomUserDetails(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.kakaoId = userEntity.getKakaoId();
        // 기본 ROLE_USER 권한 부여 (필요시 DB로부터 권한을 받아 설정)
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호 없음
    }

    @Override
    public String getUsername() {
        return kakaoId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public String getKakaoId() {
        return kakaoId;
    }
}
