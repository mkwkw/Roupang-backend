package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Integer> {
}
