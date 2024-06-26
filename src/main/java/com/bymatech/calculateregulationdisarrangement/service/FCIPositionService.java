package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionIdCreatedOnVO;
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

    Map<FCISpecieType, List<FCISpeciePosition>> groupPositionBySpecieType(List<FCISpeciePosition> position, List<FCISpecieType> fciSpecieTypes);

    Double calculateTotalValuedPosition(List<FCISpeciePosition> position, List<FCISpecieType> fciSpecieTypes);

    Double calculateTotalValuedPosition(Map<FCISpecieType, Double> summarizedPosition);

    Map<FCISpecieType, Double> getValuedPositionBySpecieType(Map<FCISpecieType, List<FCISpeciePosition>> position);

    List<FCIPositionVO> listPositionsByFCIRegulationSymbol(String symbol);

    /**
     * Lists available positions bound to a {@link com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation}
     * in order to perform inner calculations
     */
    List<FCIPosition> listPositionByFCIRegulation(String fciRegulationSymbol);

    FCIPositionVO createFCIPosition(String symbol, FCIPosition fciPosition) throws Exception;

    /**
     * Finds a Position in FCI Regulation position list
     * @param fciRegulationSymbol FCI Regulation Symbol
     * @param fciPositionId Position id
     */
    FCIPosition findFCIPositionById(String fciRegulationSymbol, Integer fciPositionId);

    FCIPositionVO findFCIPositionVOById(String fciRegulationSymbol, Integer fciPositionId) throws Exception;

    FCIPositionVO findFCIPositionVOByIdRefreshed(String fciRegulationSymbol, Integer fciPositionId) throws Exception;

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

    List<FCIPositionIdCreatedOnVO> listPositionsByFCIRegulationSymbolIdCreatedOn(String fciRegulationSymbol) throws Exception;

    /**
     * Retrieves position quantity group per month
     */
    Map<String, Integer> listPositionsByFCIRegulationSymbolMonthlyGrouped(String fciRegulationSymbol);

    /**
     * Complements retrieved position quantity per month, centering current month, with empty month quantities
     */
    Map<String, Integer> listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(String fciRegulationSymbol);

    Integer deleteFCIPosition(String fciRegulationSymbol, Integer fciPositionId);

    /**
     * Retrieves the oldest position of a FCI
     */
    FCIPositionIdCreatedOnVO getOldestPosition(String fciRegulationSymbol);

    /**
     * Lists sorted on year from current month backwards grouped record list
     */
    Map<String, Integer> listPositionsGroupedByMonthForOneYear();
}
