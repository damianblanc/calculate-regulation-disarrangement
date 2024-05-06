package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.dto.FCICompositionVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummaryPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.StatisticDTO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationService;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import com.bymatech.calculateregulationdisarrangement.service.FCIStatisticService;
import com.bymatech.calculateregulationdisarrangement.service.FCISummarizeService;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import java.time.LocalDate;
import java.time.Month;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FCISummarizeServiceImpl implements FCISummarizeService {
    @Autowired
    private FCIRegulationService fciRegulationService;
    @Autowired
    private FCIPositionService fciPositionService;
    @Autowired
    private FCIStatisticService fciStatisticService;
    @Autowired
    private FCIReportService fciReportService;
    @Autowired
    private FCIAdviceService fciAdviceService;

    @Override
    public SummarizeOverviewVO retrieveSummarizeOverview() throws Exception {
        List<FCIRegulationVO> fciRegulations = fciRegulationService.listFCIRegulations();

        /** Regulation Composition Percentages */
        Map<String, List<FCICompositionVO>> fciRegulationCompositions = fciRegulations.stream().map(fciRegulation -> Map.entry(fciRegulation.getFciSymbol(),
                        fciRegulationService.listFCIRegulationPercentages(fciRegulation.getFciSymbol())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** Regulation Position Quantity */
        Map<String, Integer> fciRegulationPositions = fciRegulations.stream()
                .map(fciRegulation -> Map.entry(fciRegulation.getFciSymbol(), fciRegulation.getPositions().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** Total Position Quantity */
        Integer totalPositionQuantity = !fciRegulations.isEmpty() ? fciRegulations.stream().map(FCIRegulationVO::getPositions).map(List::size).reduce(Integer::sum).orElseThrow() : 0;

        StatisticDTO statisticDTO = fciStatisticService.retrieveStatistics();

        /** Total Reports Quantity */
        Long reportQuantity = fciReportService.getReportQuantity();

        /** Total Advices Quantity */
        Long adviceQuantity = fciAdviceService.getAdviceQuantity();

        return SummarizeOverviewVO.builder()
                .fciRegulationQuantity(fciRegulations.size())
                .fciPositionQuantity(totalPositionQuantity)
                .fciReportsQuantity(reportQuantity)
                .fciAdvicesQuantity(adviceQuantity)
                .fciRegulationCompositions(fciRegulationCompositions)
                .fciRegulationPositionsQuantity(fciRegulationPositions)
                .fciRegulationsPerMothLastYear(fciRegulationService.listRegulationsGroupedByMonthForOneYear())
                .fciPositionsPerMothLastYear(fciPositionService.listPositionsGroupedByMonthForOneYear())
                .fciReportsPerMothLastYear(fciReportService.listReportsGroupedByMonthForOneYear())
                .fciAdvicesPerMothLastYear(fciAdviceService.listAdvicesGroupedByMonthForOneYear())
                .build();
    }

    @Override
    /** Regulations Quantity Per Month last year */
    public List<SummaryPerMonthVO> retrieveRegulationsPerMonth() {
        Map<String, Integer> monthlySortedGroupedRegulations = fciRegulationService.listRegulationsGroupedByMonthForOneYear();
        return monthlySortedGroupedRegulations.entrySet().stream().map(entry -> new SummaryPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }

    /** Position Quantity Per Month last year */
    public List<SummaryPerMonthVO> retrievePositionsPerMonth() {
        List<FCIRegulationVO> fciRegulations = fciRegulationService.listFCIRegulations();
        Map<String, Map<String, Integer>> positionsPerMonthByFCI = fciRegulations.stream().map(fciRegulation ->
                Map.entry(fciRegulation.getFciSymbol(), fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulation.getFciSymbol())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Integer> positionsPerMonthOpened = new LinkedHashMap<>();
        positionsPerMonthByFCI.get(fciRegulations.get(0).getFciSymbol()).forEach((k,v) -> positionsPerMonthOpened.put(k, 0));
        positionsPerMonthByFCI.values().forEach(m -> m.keySet().forEach(month ->
            positionsPerMonthOpened.put(month, positionsPerMonthOpened.get(month) + m.get(month))));

        return positionsPerMonthOpened.entrySet().stream()
            .map(entry -> new SummaryPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }

    @Override
    /** Advices Quantity Per Month last year */
    public List<SummaryPerMonthVO> retrieveReportsPerMonth() {
        Map<String, Integer> monthlySortedGroupedReports = fciReportService.listReportsGroupedByMonthForOneYear();
        return monthlySortedGroupedReports.entrySet().stream().map(entry -> new SummaryPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }

    /** Report Quantity Per Month last year in general, not defined by FCI Regulation */
    public List<SummaryPerMonthVO> retrieveAdvicesPerMonth() {
        Map<String, Integer> monthlySortedGroupedAdvices = fciAdviceService.listAdvicesGroupedByMonthForOneYear();
        return monthlySortedGroupedAdvices.entrySet().stream().map(entry -> new SummaryPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }

    @Override
    public List<SummaryPerMonthVO> retrieveRegulationPositionsPerMonth(String fciRegulationSymbol) {
        fciRegulationService.findFCIRegulationEntity(fciRegulationSymbol);
        Map<String, Integer> m = fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulationSymbol);
        return m.entrySet().stream().map(entry -> new SummaryPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }
}
