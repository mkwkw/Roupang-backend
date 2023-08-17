package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findAllByIsDeletedEqualsAndSellerIdx(Boolean isDeleted, Integer sellerId);

    Page<Product> findProductByOrderByPrice(Pageable pageable);

    Page<Product> findProductByOrderByPriceDesc(Pageable pageable);

    Page<Product> findProductByProductsCategoryIdxId(Integer categoryIdx, Pageable pageable);

}
