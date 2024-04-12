package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceSpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceSpecieTypeFlatFormat;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVerboseVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

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
    List<OperationAdviceSpecieType> advice(String fciRegulationSymbol, String fciPositionId) throws Exception;

    List<OperationAdviceSpecieTypeFlatFormat> adviceFlatFormat(String fciRegulationSymbol, String fciPositionId) throws Exception;

    OperationAdviceVerboseVO adviceVerbose(String fciRegulationSymbol, String fciPositionId) throws Exception;

}
