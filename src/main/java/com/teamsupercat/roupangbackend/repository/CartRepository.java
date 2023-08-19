package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("select c from Cart c where c.memberIdx.id = :member and c.productIdx.id = :product and c.isDeleted=false ")
    Optional<Cart> findByMemberIdAndProductId(Integer member, Integer product);

    @Query("select c from Cart c where c.memberIdx = :memberIdx and c.isDeleted = false")
    List<Cart> findByMemberIdxAndIsDeletedFalse(Member memberIdx);

    @Query("select c from Cart c where c.memberIdx = :memberIdx and c.isDeleted = false ")
    List<Cart> findByMemberIdxAndIsDeleted(Member memberIdx);

    @Transactional
    @Modifying
    @Query("update Cart c set c.isDeleted = true where c.memberIdx = :memberIdx and c.productIdx = :productIdx")
    void updateIsDeletedByMemberIdxAndProductIdx(Member memberIdx, Product productIdx);

    @Query("select c from Cart c where c.memberIdx = :memberIdx and c.productIdx = :productIdx and c.isDeleted = false")
    Optional<Cart> findByMemberIdxAndProductIdxAndIsDeletedFalse(Member memberIdx, Product productIdx);
}
