package project.code_wiki.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// 로그인한 유저 정보를 저장할 커스텀 객체
@Data
public class CustomUserDetails implements UserDetails {
    // 필드
    private String username;
    private String password;
    private String name;
    // 유저 상태 필드
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled; // 계정 사용 가능 여부
    private boolean isAccountNonExpired; // 계정 만료 여부
    private boolean isAccountNonLocked; // 계정 잠김 여부
    private boolean isCredentialsNonExpired; // 계정 패스워드 만료 여부
}
