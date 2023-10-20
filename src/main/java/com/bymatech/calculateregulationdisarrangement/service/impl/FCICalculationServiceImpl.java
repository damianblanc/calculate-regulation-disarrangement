package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagVerboseVO;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Comprehends FCI regulation operations implementation
 */
@Service
public class FCICalculationServiceImpl implements FCICalculationService {

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;

    @Autowired
    private FCIPositionService fciPositionService;

    @Override
    public RegulationLagOutcomeVO calculatePositionDisarrangement(String symbol, FCISpeciePositionDTO fciPosition) {
        FCIRegulation persistedFCIRegulation = fciRegulationCRUDService.findFCIRegulation(symbol);
        Map<SpecieType, Double> fciRegulationComposition = persistedFCIRegulation.getCompositionAsSpecieType();
        Map<SpecieType, List<SpeciePosition>> fciSpecieTypePosition =
                fciPositionService.groupPositionBySpecieType(fciPosition.getPosition());

        calculateDisarrangementPreconditions(fciRegulationComposition, fciSpecieTypePosition);

        Map<SpecieType, Double> regulationPercentage = persistedFCIRegulation.getCompositionAsSpecieType();
        Map<SpecieType, Double> summarizedPosition = fciPositionService.getSummarizedPosition(fciSpecieTypePosition);
        Map<SpecieType, Double> regulationValuedOverPosition = calculateRegulationValuedOverPosition(regulationPercentage, summarizedPosition);
        Map<SpecieType, Double> percentagePosition = calculatePercentagePosition(summarizedPosition);
        Map<SpecieType, Double> disarrangementPositionPercentage = calculateDisarrangementPosition(fciRegulationComposition, percentagePosition);
        Map<SpecieType, Double> disarrangementPositionValued = calculateDisarrangementValuedPosition(disarrangementPositionPercentage, summarizedPosition);

        return new RegulationLagOutcomeVO(disarrangementPositionPercentage, disarrangementPositionValued,
                percentagePosition, summarizedPosition, regulationPercentage, regulationValuedOverPosition);
    }

    private Map<SpecieType, Double> calculateRegulationValuedOverPosition(Map<SpecieType, Double> regulationPercentage,
                                                                          Map<SpecieType, Double> summarizedPosition) {
        Double totalPosition = CalculationServiceHelper.summarizePositionList(summarizedPosition);
        return regulationPercentage.entrySet().stream().map(entry ->
                        Map.entry(
                            entry.getKey(),
                            CalculationServiceHelper.calculatePercentageOverTotalValued(entry.getValue(), totalPosition)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public RegulationLagVerboseVO calculatePositionDisarrangementVerbose(String symbol, FCISpeciePositionDTO fciPosition) {
        return new RegulationLagVerboseVO(calculatePositionDisarrangement(symbol, fciPosition), fciPosition);
    }

    @Override
    public Map<SpecieType, Double> calculatePositionDisarrangementPercentages(String symbol, FCISpeciePositionDTO fciPosition) {
        return calculatePositionDisarrangement(symbol, fciPosition).getRegulationLags();
    }

    @Override
    public Map<SpecieType, Double> calculatePositionDisarrangementValued(String symbol, FCISpeciePositionDTO fciPosition) {
        return calculatePositionDisarrangement(symbol, fciPosition).getRegulationValuedLags();
    }

    private Map<SpecieType, Double> calculateDisarrangementValuedPosition(Map<SpecieType, Double> disarrangementPositionPercentage,
                                                                          Map<SpecieType, Double> summarizedPosition) {
        Double totalSummarizedPosition = summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
        return disarrangementPositionPercentage.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() * totalSummarizedPosition / 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<SpecieType, Double> calculateDisarrangementPosition(Map<SpecieType, Double> fciRegulationComposition, Map<SpecieType, Double> percentagePosition) {
        return percentagePosition.entrySet().stream().map(entry -> Map.entry(entry.getKey(), entry.getValue() - fciRegulationComposition.get(entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<SpecieType, Double> calculatePercentagePosition(Map<SpecieType, Double> summarizedPosition) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(summarizedPosition);
        return calculatePercentageBySpecieType(summarizedPosition, totalValuedPosition);
    }

    private Map<SpecieType, Double> calculatePercentageBySpecieType(Map<SpecieType, Double> summarizedPosition, Double totalValuedPosition) {
        return summarizedPosition.entrySet().stream().map(entry -> Map.entry(entry.getKey(), entry.getValue() / totalValuedPosition * 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
