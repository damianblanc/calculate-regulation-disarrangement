package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Groups operations needed on FCI Position
 */
@Service
public interface FCIPositionService {

    Map<SpecieType, List<SpeciePosition>> groupPositionBySpecieType(List<SpeciePosition> position);

    Double calculateTotalValuedPosition(List<SpeciePosition> position);

    Double calculateTotalValuedPosition(Map<SpecieType, Double> summarizedPosition);

    Map<SpecieType, Double> getSummarizedPosition( Map<SpecieType, List<SpeciePosition>> position);

    Set<FCIPositionVO> listPositionsByFCIRegulationSymbol(String symbol);

    FCIPosition createFCIPosition(String symbol, FCIPosition fciPosition) throws JsonProcessingException;

    /**
     * Finds a Position in FCI Regulation position list
     * @param symbol FCI Regulation Symbol
     * @param id Position id
     */
    FCIPosition findFCIPositionById(String symbol, Integer id);
}
