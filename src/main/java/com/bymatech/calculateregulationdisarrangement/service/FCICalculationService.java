package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Comprehends core FCI regulation operations performing base calculations
 */
@Service
public interface FCICalculationService {

    /**
     * Performs bias calculation over indicated position
     *
     * @param fciRegulationSymbol {@link FCIRegulation} defined symbol
     * @param fciPositionId       {@link FCIPosition} A Persisted raw FCIPosition to be used for further processing
     * @return A value object representation containing all relevant calculation outcomes
     * @throws JsonProcessingException Since a FCIPosition is persisted in JSON format string parsed when processing
     *                                 <p>
     *                                 Allows to work with an already persisted position
     */
    RegulationLagOutcomeVO calculatePositionBias(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception;

    FCIPositionPercentageVO calculatePositionBiasPercentages(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception;

    FCIPositionValuedVO calculatePositionBiasValued(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception;

    FCIRegulationPercentageVO calculateRegulationPercentages(String fciRegulationSymbol, String fciPositionId) throws Exception;

    FCIRegulationValuedVO calculateRegulationValued(String fciRegulationSymbol, String fciPositionId) throws Exception;

    /* Specie Type over Position */

//    /**
//     * Calculates each {@link FCISpecieType} percentage relative weight over its {@link FCIPosition}
//     *
//     * @param fciRegulationSymbol {@link FCIRegulation} defined symbol
//     * @param fciPositionId       A list of species included in Position to be processed
//     * @return A List of species with their relative percentage over position
//     * @throws JsonProcessingException
//     */
//    Map<FCISpecieType, Double> calculateSpecieTypePercentageOverPosition(String fciRegulationSymbol, String fciPositionId) throws Exception;
//
//    /**
//     * Calculates each {@link FCISpecieType} value relative weight over its {@link FCIPosition}
//     *
//     * @param fciRegulationSymbol {@link FCIRegulation} defined symbol
//     * @param fciPositionId       A list of species included in Position to be processed
//     * @return A List of species with their relative value over position
//     * @throws JsonProcessingException
//     */
//    Map<FCISpecieType, Double> calculateSpecieTypeValueOverPosition(String fciRegulationSymbol, String fciPositionId) throws Exception;
//
//    /* Specie over Specie Type */
//
//    /**
//     * Calculates each specie percentage relative weight over total position
//     *
//     * @param fciRegulationSymbol {@link FCIRegulation} defined symbol
//     * @param fciPositionId       A list of species included in Position to be processed
//     * @return A List of species with their relative percentage over position
//     * @throws JsonProcessingException
//     */
//    Map<FCISpeciePosition, Double> calculateSpeciePercentageOverSpecieType(String fciRegulationSymbol, String fciPositionId) throws JsonProcessingException;
//
//    /**
//     * Calculates each specie value relative weight over total position
//     *
//     * @param fciRegulationSymbol {@link FCIRegulation} defined symbol
//     * @param fciPositionId       A list of species included in Position to be processed
//     * @return A List of species with their relative value over position
//     * @throws JsonProcessingException
//     */
//    Map<FCISpeciePosition, Double> calculateSpecieValueOverSpecieType(String fciRegulationSymbol, String fciPositionId) throws Exception;

}
