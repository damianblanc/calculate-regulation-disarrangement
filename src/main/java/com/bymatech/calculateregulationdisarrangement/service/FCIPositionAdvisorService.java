package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVerboseVO;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Performs selected advice algorithm indicating actions to execute
 */
public interface FCIPositionAdvisorService {

    /**
     * Advices symbol indicated FCI and given position with implemented criteria
     * @param fciRegulationSymbol FCI Regulation Symbol
     * @param fciPositionId current position
     * @return A composition of disarrangement, fci lags and advices
     * @throws JsonProcessingException
     */
    OperationAdviceVerboseVO advice(String fciRegulationSymbol, String fciPositionId) throws Exception;

    OperationAdviceVerboseVO adviceVerbose(String fciRegulationSymbol, String fciPositionId) throws Exception;

}
