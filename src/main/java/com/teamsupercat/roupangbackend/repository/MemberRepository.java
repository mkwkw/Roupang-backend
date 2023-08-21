package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<Member> findById(Integer memberIdx);
}
