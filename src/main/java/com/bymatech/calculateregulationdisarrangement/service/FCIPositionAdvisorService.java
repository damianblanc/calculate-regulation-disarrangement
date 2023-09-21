package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.OperationAdviceVO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Calculates Species to buy or sale depending on defined conditions
 */
@Service
public interface FCIPositionAdvisorService {

    /**
     *
     * @param fciPosition
     * @return
     */
    Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByPrice(AdviceCalculationCriteria criteria, FCIPosition fciPosition);

    Map<SpecieType, List<OperationAdviceVO>> advicePositionSellingByHighestPriceOverAverageBased(FCIPosition fciPosition);

}
