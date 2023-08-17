package com.teamsupercat.roupangbackend.security;

import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Primary
@RequiredArgsConstructor
@Configuration
@Service

public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));

        return CustomUserDetail.builder()
                .email(member.getEmail())
                .password(member.getUserPassword())
                .build();
    }
}
