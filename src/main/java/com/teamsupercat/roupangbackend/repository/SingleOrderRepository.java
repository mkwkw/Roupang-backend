package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.SingleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleOrderRepository extends JpaRepository<SingleOrder,Integer> {
}
