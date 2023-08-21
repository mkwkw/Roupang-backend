package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {

    Optional<EmailVerification> findByVerificationCode(Integer code);
}
