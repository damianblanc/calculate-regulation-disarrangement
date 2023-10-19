package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
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
    public OperationAdviceVerboseVO advice(String symbol, FCIPosition fciPosition) throws JsonProcessingException {
        return null;
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String symbol, FCIPosition fciPosition) throws JsonProcessingException {
        return null;
    }
}
