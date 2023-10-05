package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVerboseVO;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Performs selected advice algorithm indicating actions to execute
 */
public interface FCIPositionAdvisorService {

    OperationAdviceVerboseVO advice(FCIPosition fciPosition) throws JsonProcessingException;

    OperationAdviceVerboseVO adviceVerbose(FCIPosition fciPosition) throws JsonProcessingException;

}
