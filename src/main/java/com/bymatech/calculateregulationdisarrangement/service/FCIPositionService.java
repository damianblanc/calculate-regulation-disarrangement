package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Groups operations needed on FCI Position
 */
@Service
public interface FCIPositionService {

    Map<SpecieType, List<SpeciePosition>> groupPositionBySpecieType(List<SpeciePosition> position);

    Double calculateTotalValuedPosition(List<SpeciePosition> position);

    Double calculateTotalValuedPosition(Map<SpecieType, Double> summarizedPosition);

    Map<SpecieType, Double> getSummarizedPosition( Map<SpecieType, List<SpeciePosition>> position);
}
