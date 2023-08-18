package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Page<Product> findAllByIsDeletedAndSellerIdx(Boolean isDeleted, Seller seller, Pageable pageable);

    Page<Product> findProductsByOrderByCreatedAtDesc(Pageable pageable);

    Page<Product> findProductByOrderByPrice(Pageable pageable);

    Page<Product> findProductByOrderByPriceDesc(Pageable pageable);

    Page<Product> findProductByProductsCategoryIdxId(Integer categoryIdx, Pageable pageable);

    List<Product> findProductByProductsCategoryIdxId(Integer categoryIdx);

    Page<Product> findProductByProductsCategoryIdxIdOrderByPrice(Integer categoryIdx, Pageable pageable);

    Page<Product> findProductByProductsCategoryIdxIdOrderByPriceDesc(Integer categoryIdx, Pageable pageable);

    Product findProductById(int productIdx);
}
