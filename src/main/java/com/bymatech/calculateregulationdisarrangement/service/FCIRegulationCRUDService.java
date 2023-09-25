package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
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
    FCIRegulation createFCIRegulation(FCIRegulation fciRegulation);

    /**
     * Deletes a FCIRegulation indicated by its symbol
     * @return
     */
    FCIRegulation deleteFCIRegulation(String symbol);

    /**
     * Updates a FCIRegulation indicated by its symbol
     * @return Updated FCIRegulation
     */
    FCIRegulation updateFCIRegulation(FCIRegulation fciRegulation);

    /**
     * Finds a FCIRegulation indicated by its symbol
     * @return Found FCIRegulation
     */
    FCIRegulation findFCIRegulation(String symbol);

    /**
     * Finds a FCIRegulation indicated by its symbol
     * @return An Optional of FCIRegulation
     */
    Optional<FCIRegulation> findFCIRegulationOptional(String symbol);

    /**
     * Finds or Creates a FCIRegulation indicated by its symbol
     * @return created FCIRegulation
     */
    FCIRegulation findOrCreateFCIRegulation(FCIRegulation fciRegulation);

    /**
     * List all FCIRegulations available
     * @return All created FCIRegulations
     */
    List<FCIRegulation> listFCIRegulations();
}
