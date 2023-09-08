package com.bymatech.calculateregulationdisarrangement.service;

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

    RegulationLagOutcomeVO calculatePositionDisarrangement(FCIPosition fciPosition);

    RegulationLagVerboseVO calculatePositionDisarrangementVerbose(FCIPosition fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementPercentages(FCIPosition fciPosition);

    Map<SpecieType, Double> calculatePositionDisarrangementValued(FCIPosition fciPosition);

}
