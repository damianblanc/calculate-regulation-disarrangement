package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVerboseVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

/**
 * Advices Volume Max Trading criteria implementation
 */
@Service
public class FCIPositionCriteriaVolumeMaxTradingService implements FCIPositionAdvisorService {
    @Override
    public OperationAdviceVerboseVO advice(String symbol, FCISpeciePositionDTO fciPosition) throws JsonProcessingException {
        return null;
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String symbol, FCISpeciePositionDTO fciPosition) throws JsonProcessingException {
        return null;
    }
}
