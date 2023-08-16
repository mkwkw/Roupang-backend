package com.teamsupercat.roupangbackend.repository.product;

import com.teamsupercat.roupangbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
