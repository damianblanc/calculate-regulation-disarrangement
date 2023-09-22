package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;

import java.util.Collection;
import java.util.Map;

/**
 * Performs selected advice algorithm indicating actions to execute
 */
@FunctionalInterface
public interface FCIPositionAdvisor {

    Map<SpecieType, Collection<OperationAdviceVO>> advice(FCIPosition fciPosition);

}
