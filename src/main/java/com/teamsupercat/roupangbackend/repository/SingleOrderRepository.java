package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.SingleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingleOrderRepository extends JpaRepository<SingleOrder, Integer> {

    List<SingleOrder> findByProductIdx(Product productId);

}
