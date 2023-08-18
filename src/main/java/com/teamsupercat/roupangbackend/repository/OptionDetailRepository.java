package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetail, Integer> {
    OptionDetail findOptionDetailById(int optionDetailIdx);

    List<OptionDetail> findOptionDetailByOptionTypeNameIdx(Integer id);
}
