package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagVerboseVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Comprehends FCI regulation operations
 */
@Service
public interface FCICalculationService {

    RegulationLagOutcomeVO calculatePositionDisarrangement(String symbol, FCIPosition fciPosition);

    RegulationLagVerboseVO calculatePositionDisarrangementVerbose(String symbol, FCIPosition fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementPercentages(String symbol, FCIPosition fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementValued(String symbol, FCIPosition fciPosition);
}
