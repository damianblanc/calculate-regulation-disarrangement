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

/**
 * Comprehends FCI regulation operations implementation
 */
@Service
public class FCICalculationServiceImpl implements FCICalculationService {

    @Override
    public RegulationLagOutcomeVO calculatePositionDisarrangement(FCIPosition fciPosition) {
        Map<SpecieType, Double> fciRegulationComposition = fciPosition.getFciRegulation().getComposition();
        Map<SpecieType, List<SpeciePosition>> fciSpecieTypePosition = groupPositionBySpecieType(fciPosition.getFciPositionList());

        calculateDisarrangementPreconditions(fciRegulationComposition, fciSpecieTypePosition);
        Map<SpecieType, Double> summarizedPosition = getSummarizedPosition(fciSpecieTypePosition);
        Map<SpecieType, Double> percentagePosition = calculatePercentagePosition(summarizedPosition);
        Map<SpecieType, Double> disarrangementPositionPercentage = calculateDisarrangementPosition(fciRegulationComposition, percentagePosition);
        Map<SpecieType, Double> disarrangementPositionValued = calculateDisarrangementValuedPosition(disarrangementPositionPercentage, summarizedPosition);

        return new RegulationLagOutcomeVO(disarrangementPositionPercentage, disarrangementPositionValued, percentagePosition, summarizedPosition);
    }

    private Map<SpecieType, Double> calculateDisarrangementValuedPosition(Map<SpecieType, Double> disarrangementPositionPercentage,
                                                                          Map<SpecieType, Double> summarizedPosition) {
        Double totalSummarizedPosition = summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
        return disarrangementPositionPercentage.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() * totalSummarizedPosition / 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    /**
     * Performs required business validations before processing FCI position for disarrangement searching
     * @param fciRegulationComposition Comprehends FCI Specie Type and their percentages definition
     * @param fciSpecieTypePosition Comprehends incoming FCI current Specie Type position
     *
     * Expected Validations:
     * fciRegulationComposition must close to 100%
     * All Specie type indicated in FCI Regulation Percentage composition are expected to be received for processing
     */
    private void calculateDisarrangementPreconditions(Map<SpecieType, Double> fciRegulationComposition,
                                                      Map<SpecieType, List<SpeciePosition>> fciSpecieTypePosition) {
        Double percentageSumReduction = fciRegulationComposition.values().stream().reduce(Double::sum).orElseThrow();

        /** fciRegulationComposition must close to 100% */
        if (!Constants.TOTAL_PERCENTAGE.equals(percentageSumReduction)) {
            throw new FailedValidationException(ExceptionMessage.TOTAL_PERCENTAGE.msg);
        }

        /** All Specie type indicated in FCI Regulation Percentage composition are expected to be received for processing */
        fciRegulationComposition.keySet().forEach(fciRegulationSpecieType -> {
            if (fciSpecieTypePosition.keySet().stream()
                    .noneMatch(fciPositionSpecieType -> fciPositionSpecieType == fciRegulationSpecieType)) {
                throw new IllegalArgumentException(String.format(ExceptionMessage.REGULATION_SPECIE_TYPE_DOES_NOT_MATCH.msg, fciRegulationSpecieType));
            }
        });
    }
}
