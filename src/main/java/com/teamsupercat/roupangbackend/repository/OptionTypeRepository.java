package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Integer> {
    List<OptionType> findOptionTypeByProductIdx(Integer productIdx);

    List<OptionType> findAllByProductIdx(Integer productId);

}
