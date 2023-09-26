package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FCIRegulationRepository extends JpaRepository<FCIRegulation, Integer> {

    Optional<FCIRegulation> findByName(String name);



    Optional<FCIRegulation> findBySymbol(String symbol);

//    @Query("SELECT f FROM FCIRegulation" +
//            "JOIN f.composition c ON c.fciRegulation IN :p")
//    List<FCIComposition> getFCIRegulationCompositions(@Param("p") String symbol);
}
