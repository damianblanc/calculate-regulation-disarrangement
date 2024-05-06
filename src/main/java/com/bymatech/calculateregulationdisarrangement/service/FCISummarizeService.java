package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.SummaryPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import java.util.Map;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FCISummarizeService {

    SummarizeOverviewVO retrieveSummarizeOverview() throws Exception;

    /**
     * Searches for regulations created grouped by month
     */
    List<SummaryPerMonthVO> retrieveRegulationsPerMonth();

    /**
     * Searches for positions created grouped by month
     */
    List<SummaryPerMonthVO> retrievePositionsPerMonth();

    /**
     * Calculates uploaded positions per month for a {@link com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation}
     */
    List<SummaryPerMonthVO> retrieveRegulationPositionsPerMonth(String fciRegulationSymbol);

    /**
     * Searches for reports created grouped by month
     */
    List<SummaryPerMonthVO> retrieveReportsPerMonth();

    /**
     * Searches for advices created grouped by month
     */
    List<SummaryPerMonthVO> retrieveAdvicesPerMonth();
}
