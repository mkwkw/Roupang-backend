package com.teamsupercat.roupangbackend.security;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.RefreshTokenRepository;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private String secretKey = "Super-Coding";


    final private UserDetailsService userDetailsService;

    final private MemberRepository memberRepository;

    final private  RefreshTokenRepository refreshTokenRepository;
    final long AccessTokenValidMilliSecond =  1000L * 60 * 60; // 1시간
    final long RefreshTokenValidMilliSecond = 1000L *60 * 60 *24 * 7; //  1주일

    @PostConstruct
    public void setUp(){
        secretKey = Base64.getEncoder()
                .encodeToString(secretKey.getBytes());
        }
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token==null ? null : token.substring(7);
    }

        public Integer getMemberIdx(String jwtToken){
            log.info("getMemberIdx jwtToken {}" ,jwtToken);
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            Integer memberIdx = (Integer) claims.get("idx");
          return memberIdx;
        }
    public String createAccessToken(Member member){
        Claims claims = Jwts.claims().setSubject("memberToken");
        claims.put("idx" ,member.getId());
        claims.put("email",member.getEmail());
        claims.put("nickname",member.getNickname());
        claims.put("phone_number",member.getPhoneNumber());
        claims.put("address",member.getAddress());
        claims.put("member_img",member.getMemberImg());
        claims.put("created_at",timestampToString(member.getCreatedAt()));
        claims.put("updated_at",timestampToString(member.getUpdatedAt()));
        claims.put("user_point",member.getUserPoint());

        Date now = new Date();

        return Jwts.builder()
                .setIssuer("superCat")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ AccessTokenValidMilliSecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public String createRefreshToken(Member member){
        Claims claims = Jwts.claims().setSubject("memberToken");
        claims.put("idx" ,member.getId());
        claims.put("email",member.getEmail());
        claims.put("nickname",member.getNickname());
        claims.put("phone_number",member.getPhoneNumber());
        claims.put("address",member.getAddress());
        claims.put("member_img",member.getMemberImg());
        claims.put("created_at",timestampToString(member.getCreatedAt()));
        claims.put("updated_at",timestampToString(member.getUpdatedAt()));
        claims.put("user_point",member.getUserPoint());

        Date now = new Date();

        return Jwts.builder()
                .setIssuer("superCat")
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
        String email= Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("email",String.class);
        return email;
    }

    public String timestampToString(Instant instant){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

        if(instant != null)return formatter.format(instant);

        else return null;
    }


    public Member getMember(String jwtToken){
        String memberEmail = getEmail(jwtToken);
        return memberRepository.findByEmail(memberEmail).orElseThrow(()
                -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND_EMAIL));
    }
}

