package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginFailRepository extends JpaRepository<LoginAttempt, Integer> {
    Optional<LoginAttempt> findByDomainAndEmail(String domain, String Email);
}
