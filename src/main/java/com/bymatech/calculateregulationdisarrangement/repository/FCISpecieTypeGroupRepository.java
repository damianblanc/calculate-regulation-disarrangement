package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FCISpecieTypeGroupRepository extends JpaRepository<FCISpecieTypeGroup, Integer> {

    @Query("SELECT stg FROM FCISpecieTypeGroup stg WHERE stg.name = :groupName")
    Optional<FCISpecieTypeGroup> findByName(@Param("groupName") String groupName);

}