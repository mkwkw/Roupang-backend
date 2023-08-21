package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionDetail;
import com.teamsupercat.roupangbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetail, Integer> {
    OptionDetail findOptionDetailById(int optionDetailIdx);

    List<OptionDetail> findOptionDetailByOptionTypeNameIdx(Integer id);

    //@Query(name = "SELECT o FROM OptionDetail o JOIN o.productIdx WHERE o.optionDetailName = :optionDetailName")
    List<OptionDetail> findOptionDetailByOptionDetailName(String optionDetailName);

    void deleteAllByOptionTypeIdx(Integer id);

    List<OptionDetail> findAllByProductIdx(Product productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE OptionDetail o SET o.isDeleted = true WHERE o.productIdx=:idx ")
    void deleteOptionDetail(@Param("idx") Product productId);
}
