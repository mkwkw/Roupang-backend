package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.GroupedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupedOrderRepository extends JpaRepository<GroupedOrder,Integer> {
}
