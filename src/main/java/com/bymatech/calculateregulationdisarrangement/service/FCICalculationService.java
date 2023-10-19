package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagVerboseVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Comprehends FCI regulation operations
 */
@Service
public interface FCICalculationService {

    RegulationLagOutcomeVO calculatePositionDisarrangement(String symbol, FCIPositionDTO fciPosition);

    RegulationLagVerboseVO calculatePositionDisarrangementVerbose(String symbol, FCIPositionDTO fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementPercentages(String symbol, FCIPositionDTO fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementValued(String symbol, FCIPositionDTO fciPosition);
}
