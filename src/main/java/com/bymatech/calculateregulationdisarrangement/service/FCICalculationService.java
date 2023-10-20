package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Comprehends FCI regulation operations
 */
@Service
public interface FCICalculationService {

    RegulationLagOutcomeVO calculatePositionDisarrangement(String symbol, FCISpeciePositionDTO fciPosition);

    RegulationLagOutcomeVO calculatePositionBiasById(String symbol, String id) throws JsonProcessingException;

    RegulationLagVerboseVO calculatePositionDisarrangementVerbose(String symbol, FCISpeciePositionDTO fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementPercentages(String symbol, FCISpeciePositionDTO fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementValued(String symbol, FCISpeciePositionDTO fciPosition);

    FCIPositionPercentageVO calculatePositionBiasPercentages(String symbol, String id) throws JsonProcessingException;

    FCIPositionValuedVO calculatePositionBiasValued(String symbol, String id) throws JsonProcessingException;

    FCIRegulationPercentageVO calculateRegulationPercentages(String symbol, String id) throws JsonProcessingException;

    FCIRegulationValuedVO calculateRegulationValued(String symbol, String id) throws JsonProcessingException;
}
