package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FCIRegulationRepository extends JpaRepository<FCIRegulation, Integer> {

    Optional<FCIRegulation> findByName(String name);

    Optional<FCIRegulation> findBySymbol(String symbol);

    @Query("SELECT f FROM FCIRegulation f WHERE f.symbol = :symbol")
    List<FCIRegulation> listBySymbol(@Param("symbol") String symbol);
}
