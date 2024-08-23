package bigbrother.slimdealz.auth;

import bigbrother.slimdealz.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
        this.attributes = new HashMap<>();
    }

    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.member = member;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    // 토큰에 포함될 사용자 정보
    public Map<String, Object> getMemberInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", member.getName());
        info.put("kakao_Id", member.getKakao_Id());
        info.put("role", member.getRole().getValue());
        return info;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 비밀번호가 필요하지 않다면 null 반환
    @Override
    public String getPassword() {
        return null;  // 비밀번호가 필요하지 않다면 null 반환
    }

    @Override
    public String getUsername() {
        return member.getName();
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
}
