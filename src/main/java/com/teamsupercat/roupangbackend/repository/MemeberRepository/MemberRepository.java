package com.teamsupercat.roupangbackend.repository.MemeberRepository;

import com.teamsupercat.roupangbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByEmail(String email);
}
