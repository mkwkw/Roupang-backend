package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Integer> {

   Boolean existsByMemberIdx(Member member);

   Seller findSellerByMemberIdx(Member member);

}
