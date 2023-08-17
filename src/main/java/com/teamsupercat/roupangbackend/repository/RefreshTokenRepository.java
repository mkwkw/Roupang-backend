package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByMemberIdx(Integer memberIdx);

    Boolean existsByMemberIdx(Integer memberIdx);
}
