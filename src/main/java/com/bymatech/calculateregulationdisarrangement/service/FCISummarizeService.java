package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.PositionPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FCISummarizeService {

    SummarizeOverviewVO retrieveSummarizeOverview() throws Exception;

    List<PositionPerMonthVO> retrievePositionsPerMonth();

    /**
     * Calculates uploaded positions per month for a {@link com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation}
     */
    List<PositionPerMonthVO> retrieveRegulationPositionsPerMonth(String fciRegulationSymbol);
}
