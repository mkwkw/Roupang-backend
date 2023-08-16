package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
}
