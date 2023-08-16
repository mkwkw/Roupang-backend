package com.teamsupercat.roupangbackend.repository.cart;

import com.teamsupercat.roupangbackend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
}
