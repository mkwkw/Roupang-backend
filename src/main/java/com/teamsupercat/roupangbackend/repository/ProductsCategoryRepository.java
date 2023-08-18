package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsCategoryRepository extends JpaRepository<ProductsCategory,Integer> {
}
