package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.dto.FCICompositionVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPercentageVO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.service.FCISummarizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Integer totalPositionQuantity = fciRegulations.stream().map(FCIRegulationVO::getPositions).map(List::size).reduce(Integer::sum).orElseThrow();

        /** Regulation Position Quantity Per Month last Year */
        Map<String, Map<String, Integer>> positionsPerMonth = fciRegulations.stream().map(fciRegulation ->
            Map.entry(fciRegulation.getFciSymbol(), fciPositionService.listPositionsByFCIRegulationSymbolMonthlyGroupedTotal(fciRegulation.getFciSymbol())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//        Map<String, Integer> positionsPerMonthOpened = positionsPerMonth.entrySet().stream().map(entry ->
//                Map.entry(entry.getValue()., entry.getValue().)
//                )

        // Retrieve 5 FCI with most positions per day


        return SummarizeOverviewVO.builder()
                .fciRegulationQuantity(fciRegulations.size())
                .fciRegulationCompositions(fciRegulationCompositions)
                .fciRegulationPositionsQuantity(fciRegulationPositions)
                .fciPositionQuantity(totalPositionQuantity)
//                .fciPositionsPerMothLastYear(positionsPerMonthOpened)
                .build();
    }
}
