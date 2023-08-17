package com.teamsupercat.roupangbackend.security;

import com.teamsupercat.roupangbackend.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private String secretKey = "Super-Coding";

    final private UserDetailsService userDetailsService;
    final long AccessTokenValidMilliSecond =  1000L * 60 * 60; // 1시간
    final long RefreshTokenValidMilliSecond = 1000L *60 * 60 *24 * 7; //  1주일

    @PostConstruct
    public void setUp(){
        secretKey = Base64.getEncoder()
                .encodeToString(secretKey.getBytes());
        }
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public String createAccessToken(Member member){
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("idx",member.getId());
        claims.put("nickname",member.getNickname());
        claims.put("phone_number",member.getPhoneNumber());
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ AccessTokenValidMilliSecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Member member){
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("idx", member.getId());
        claims.put("idx",member.getId());
        claims.put("nickname",member.getNickname());
        claims.put("phone_number",member.getPhoneNumber());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ RefreshTokenValidMilliSecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validToken(String jwtToken){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            Date now = new Date();
            return claims.getExpiration().after(now);
        }catch (Exception e){
            return false;
        }
    }
    public Authentication getAuthentication(String jwtToken){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "",
                userDetails.getAuthorities());
    }


    public String getEmail(String jwtToken){
        String email = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
        return email;
    }
    }

