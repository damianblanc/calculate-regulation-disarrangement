package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import org.springframework.stereotype.Service;

/**
 * Comprehends FCI regulation operations
 */
@Service
public interface FCICalculationService {

    RegulationLagOutcomeVO calculateDisarrangement(FCIPosition fciPosition);
}
