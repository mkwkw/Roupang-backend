package com.teamsupercat.roupangbackend.repository.member;

import com.teamsupercat.roupangbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findByEmail(String email);
}
