package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Comprehends CRUD operations over {@link FCIRegulation}
 */
@Service
public interface FCIRegulationCRUDService {

    /**
     * Creates a new FCIRegulation
     * @return created FCIRegulation
     */
    FCIRegulationVO createFCIRegulation(FCIRegulation fciRegulation);

    /**
     * Deletes a FCIRegulation indicated by its symbol
     */
    String deleteFCIRegulation(String fciSymbol);

    /**
     * Updates a FCIRegulation indicated by its symbol
     * @return Updated FCIRegulation
     */
    FCIRegulationVO updateFCIRegulation(FCIRegulation fciRegulation);

    /**
     * Finds a FCIRegulation indicated by its symbol
     * @return Found FCIRegulation
     */
    FCIRegulationVO findFCIRegulation(String symbol);

    /**
     * Finds a FCIRegulation indicated by its symbol
     * @return Found FCIRegulation
     */
    FCIRegulation findFCIRegulationEntity(String symbol);

    /**
     * Finds a FCIRegulation indicated by its symbol
     * @return An Optional of FCIRegulation
     */
    Optional<FCIRegulation> findFCIRegulationOptionalEntity(String symbol);

    /**
     * Finds or Creates a FCIRegulation indicated by its symbol
     *
     * @return created FCIRegulation
     */
    FCIRegulation findOrCreateFCIRegulationEntity(FCIRegulationDTO fciRegulationDTO);

    /**
     * List all FCIRegulations available
     * @return All created FCIRegulations
     */
    List<FCIRegulationVO> listFCIRegulations();

    List<String> listFCIRegulationSymbols();

    List<FCIRegulationSymbolAndNameVO> listFCIRegulationSymbolsAndNames();

    List<FCICompositionVO> listFCIRegulationPercentages(String fciRegulationSymbol);
}
