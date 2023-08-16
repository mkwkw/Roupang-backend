package com.teamsupercat.roupangbackend.repository.paymentMethod;

import com.teamsupercat.roupangbackend.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Integer> {
}
