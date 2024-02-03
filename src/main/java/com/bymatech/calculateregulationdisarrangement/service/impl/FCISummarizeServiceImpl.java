package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCICompositionVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationVO;
import com.bymatech.calculateregulationdisarrangement.dto.PositionPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIRegulationRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.service.FCISummarizeService;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FCISummarizeServiceImpl implements FCISummarizeService {

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;


    @Autowired
    private FCIPositionService fciPositionService;

    @Override
    public SummarizeOverviewVO retrieveSummarizeOverview() throws Exception {
        List<FCIRegulationVO> fciRegulations = fciRegulationCRUDService.listFCIRegulations();

        /** Regulation Composition Percentages */
        Map<String, List<FCICompositionVO>> fciRegulationCompositions = fciRegulations.stream().map(fciRegulation -> Map.entry(fciRegulation.getFciSymbol(),
                        fciRegulationCRUDService.listFCIRegulationPercentages(fciRegulation.getFciSymbol())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** Regulation Position Quantity */
        Map<String, Integer> fciRegulationPositions = fciRegulations.stream()
                .map(fciRegulation -> Map.entry(fciRegulation.getFciSymbol(), fciRegulation.getPositions().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** Total Position Quantity*/
        Integer totalPositionQuantity = !fciRegulations.isEmpty() ? fciRegulations.stream().map(FCIRegulationVO::getPositions).map(List::size).reduce(Integer::sum).orElseThrow() : 0;

        /** Regulation Position Quantity Per Month last year */
        Map<String, Map<String, Integer>> positionsPerMonthByFCI = fciRegulations.stream().map(fciRegulation ->
            Map.entry(fciRegulation.getFciSymbol(), fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulation.getFciSymbol())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Integer> positionsPerMonthOpened = new LinkedHashMap<>();
        DateOperationHelper.months.forEach(month -> positionsPerMonthOpened.put(month, 0));
        positionsPerMonthByFCI.values().forEach(m -> m.keySet().forEach(month ->
                positionsPerMonthOpened.put(month, positionsPerMonthOpened.get(month) + m.get(month))));

            //        Posiciones por mes
//
//        Retrieve 5 FCI with most positions per day  (then  month - year)
//        5 FCIs fluctuations related to its regulations, related to a specie type group?


        return SummarizeOverviewVO.builder()
                .fciRegulationQuantity(fciRegulations.size())
                .fciRegulationCompositions(fciRegulationCompositions)
                .fciRegulationPositionsQuantity(fciRegulationPositions)
                .fciPositionQuantity(totalPositionQuantity)
                .fciPositionsPerMothLastYear(positionsPerMonthOpened)
                .build();
    }

    @Override
    /** Regulation Position Quantity Per Month last year */
    public List<PositionPerMonthVO> retrievePositionsPerMonth() {
        List<FCIRegulationVO> fciRegulations = fciRegulationCRUDService.listFCIRegulations();
        Map<String, Map<String, Integer>> positionsPerMonthByFCI = fciRegulations.stream().map(fciRegulation ->
                        Map.entry(fciRegulation.getFciSymbol(), fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulation.getFciSymbol())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Integer> positionsPerMonthOpened = new LinkedHashMap<>();
        positionsPerMonthByFCI.get(fciRegulations.get(0).getFciSymbol()).forEach((k,v) -> positionsPerMonthOpened.put(k, 0));
        positionsPerMonthByFCI.values().forEach(m -> m.keySet().forEach(month ->
                positionsPerMonthOpened.put(month, positionsPerMonthOpened.get(month) + m.get(month))));

        return positionsPerMonthOpened.entrySet().stream()
                .map(entry -> new PositionPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }

    @Override
    public List<PositionPerMonthVO> retrieveRegulationPositionsPerMonth(String fciRegulationSymbol) {
        fciRegulationCRUDService.findFCIRegulationEntity(fciRegulationSymbol);
        Map<String, Integer> m = fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulationSymbol);
        return m.entrySet().stream().map(entry -> new PositionPerMonthVO(entry.getKey(), entry.getValue())).toList();
    }
}
