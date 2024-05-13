package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FCISpecieToSpecieTypeRepository extends JpaRepository<FCISpecieToSpecieType, Integer> {

    Optional<FCISpecieToSpecieType> findBySpecieSymbol(String specieSymbol);

    @Query("SELECT s FROM FCISpecieToSpecieType s WHERE s.fciSpecieType = :fciSpecieType")
    List<FCISpecieToSpecieType> listBySpecieType(@Param("fciSpecieType") FCISpecieType specieType);

    @Query("SELECT s FROM FCISpecieToSpecieType s WHERE s.specieSymbol = :specieSymbol")
    Optional<FCISpecieToSpecieType> findByName(@Param("specieSymbol") String symbol);

}
