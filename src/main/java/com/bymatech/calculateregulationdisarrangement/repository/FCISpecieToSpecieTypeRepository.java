package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieToSpecieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FCISpecieToSpecieTypeRepository extends JpaRepository<FCISpecieToSpecieType, Integer> {

    Optional<FCISpecieToSpecieType> findByName(String name);

    @Query("SELECT stty FROM FCISpecieToSpecieType stty WHERE stty.fci_specie_type_id = :fciSpecieTypeId")
    List<FCISpecieToSpecieType> listBySpecieTypeId(@Param("fciSpecieTypeId") Integer specieTypeId);

}
