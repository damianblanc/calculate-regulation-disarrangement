package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Performs selected advice algorithm indicating actions to execute
 */
@Service
public interface FCIPositionAdvisorService {

    Map<SpecieType, Collection<OperationAdviceVO>> advice(FCIPosition fciPosition);

}
