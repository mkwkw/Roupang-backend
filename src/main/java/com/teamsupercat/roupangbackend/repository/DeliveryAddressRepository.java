package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Integer> {
}
