package com.teamsupercat.roupangbackend.repository.payment;

import com.teamsupercat.roupangbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
