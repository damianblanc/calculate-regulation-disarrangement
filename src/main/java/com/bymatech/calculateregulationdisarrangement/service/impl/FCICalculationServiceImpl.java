package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FCICalculationServiceImpl implements FCICalculationService {
    @Override
    public RegulationLagOutcomeVO calculateDisarrangement(FCIPosition fciPosition) {
        Map<SpecieType, Double> composition = fciPosition.getRegulation().getComposition();

        calculateDisarrangementPreconditions(composition);
        Map<SpecieType, Double> summarizedPosition = getSummarizedPosition(groupPositionBySpecieType(fciPosition.getPosition()));
        Map<SpecieType, Double> percentagePosition = calculatePercentagePosition(summarizedPosition);
        Map<SpecieType, Double> disarrangementPosition = calculateDisarrangementPosition(composition, percentagePosition);
        return new RegulationLagOutcomeVO(disarrangementPosition,summarizedPosition);
    }

    private Map<SpecieType, Double> calculateDisarrangementPosition(Map<SpecieType, Double> composition, Map<SpecieType, Double> percentagePosition) {
        return percentagePosition.entrySet().stream().map(entry -> Map.entry(entry.getKey(), composition.get(entry.getKey()) - entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<SpecieType, Double> calculatePercentagePosition(Map<SpecieType, Double> summarizedPosition) {
        Double totalValuedPosition = calculateTotalValuedPosition(summarizedPosition);
        return calculatePercentageBySpecieType(summarizedPosition, totalValuedPosition);
    }

    private Map<SpecieType, Double> calculatePercentageBySpecieType(Map<SpecieType, Double> summarizedPosition, Double totalValuedPosition) {
        return summarizedPosition.entrySet().stream().map(entry -> Map.entry(entry.getKey(), entry.getValue() / totalValuedPosition))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Double calculateTotalValuedPosition(Map<SpecieType, Double> summarizedPosition) {
        return summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
    }

    private Map<SpecieType, Double> getSummarizedPosition( Map<SpecieType, List<SpeciePosition>> position) {
        return position.entrySet().stream()
                .map(entry ->
                        Map.entry(entry.getKey(),
                                entry.getValue().stream()
                                        .map(SpeciePosition::valuePosition)
                                        .reduce(Double::sum).orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<SpecieType, List<SpeciePosition>> groupPositionBySpecieType(List<SpeciePosition> position) {
        return position.stream().collect(Collectors.groupingBy(SpeciePosition::getSpecieType));
    }

    private void calculateDisarrangementPreconditions(Map<SpecieType, Double> composition) {
        Double percentageSumReduction = composition.values().stream()
                .reduce(Double::sum)
                .orElseThrow(() -> new FailedValidationException(ExceptionMessage.INTERNAL_PERCENTAGE_SUM_REDUCTION.msg));

        if (!Constants.TOTAL_PERCENTAGE.equals(percentageSumReduction)) {
            throw new FailedValidationException(ExceptionMessage.TOTAL_PERCENTAGE.msg);
        }
    }
}
