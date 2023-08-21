package com.teamsupercat.roupangbackend.repository;

import com.teamsupercat.roupangbackend.entity.OptionTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionTypeNameRepository extends JpaRepository<OptionTypeName, Integer> {
    OptionTypeName findOptionTypeNameById(Integer optionTypeNameIdx);

    Optional<OptionTypeName> findOptionTypeNameByOptionName(String optionTypeName);
}
