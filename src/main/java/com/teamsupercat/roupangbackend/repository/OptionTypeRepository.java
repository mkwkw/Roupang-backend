package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Integer> {
    List<OptionType> findOptionTypeByProductIdx(Integer productIdx);

    List<OptionType> findAllByProductIdx(Integer productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE OptionType o SET o.isDeleted = true WHERE o.productIdx=:idx ")
    void deleteOptionType(@Param("idx") Integer productId);
}
