package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Performs selected advice algorithm indicating actions to execute
 */
@Service
public interface FCIPositionAdvisorService {

    Map<SpecieType, Collection<OperationAdviceVO>> advice(FCIPosition fciPosition) throws JsonProcessingException;

}
