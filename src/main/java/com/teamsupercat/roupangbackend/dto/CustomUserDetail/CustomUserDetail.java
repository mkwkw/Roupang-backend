package com.teamsupercat.roupangbackend.dto.CustomUserDetail;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomUserDetail implements UserDetails {
    private String email;

    private String password;

    private List<String> authority;

    private Integer memberIdx;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {   // 해당 유저 롤 부여
//        return authority.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Integer getIdx(){
        return this.memberIdx;
    }
}
