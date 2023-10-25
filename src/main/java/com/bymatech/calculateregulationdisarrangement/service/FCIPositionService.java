package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
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

    Map<FCISpecieType, List<FCISpeciePosition>> groupPositionBySpecieType(List<FCISpeciePosition> position);

    Double calculateTotalValuedPosition(List<FCISpeciePosition> position);

    Double calculateTotalValuedPosition(Map<FCISpecieType, Double> summarizedPosition);

    Map<FCISpecieType, Double> getValuedPositionBySpecieType(Map<FCISpecieType, List<FCISpeciePosition>> position);

    Set<FCIPositionVO> listPositionsByFCIRegulationSymbol(String symbol);

    FCIPosition createFCIPosition(String symbol, FCIPosition fciPosition) throws JsonProcessingException;

    /**
     * Finds a Position in FCI Regulation position list
     * @param fciRegulationSymbol FCI Regulation Symbol
     * @param fciPositionId Position id
     */
    FCIPosition findFCIPositionById(String fciRegulationSymbol, Integer fciPositionId);

    /**
     * Updates Current Market Price to all species in Position
     * @param fciPosition Represents current working Position
     * @param refresh Indicates to retrieve prices from market instead of working with last retrieve ones
     */
    List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition, Boolean refresh) throws Exception;

    /**
     * Updates Current Market Price to all species in Position
     * @param fciPosition Represents current working Position
     */
    List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition) throws Exception;
}
