package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.SingleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SingleOrderRepository extends JpaRepository<SingleOrder,Integer> {

    List<SingleOrder> findByProductIdx(Product productId);

}
