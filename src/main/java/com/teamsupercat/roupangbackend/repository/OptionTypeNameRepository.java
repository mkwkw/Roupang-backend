package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionTypeNameRepository extends JpaRepository<OptionTypeName, Integer> {
    OptionTypeName findOptionTypeNameById(Integer optionTypeNameIdx);
}
