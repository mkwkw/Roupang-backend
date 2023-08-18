package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.GroupedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupedOrderRepository extends JpaRepository<GroupedOrder, Integer> {

    GroupedOrder findFirstByGroupedId(String groupedId);

    List<GroupedOrder> findByGroupedId(String groupedId);


}
