package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.exception.FailedValidationException;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationService;
import com.bymatech.calculateregulationdisarrangement.service.FCISpecieTypeGroupService;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
import com.bymatech.calculateregulationdisarrangement.util.Constants;
import com.bymatech.calculateregulationdisarrangement.util.DomainExtractionHelper;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Comprehends FCI regulation operations implementation
 */
@Service
public class FCICalculationServiceImpl implements FCICalculationService {

    @Autowired
    private FCIRegulationService fciRegulationService;

    @Autowired
    private FCIPositionService fciPositionService;

    @Autowired
    private FCISpecieTypeGroupService fciSpecieTypeGroupService;

//TODO:Create a cache and load a refreshed market price indicated position, in order to avoid processing when asking to flavours
    @Override
    public RegulationLagOutcomeVO calculatePositionBias(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception {
        FCIRegulation fciRegulation = fciRegulationService.findFCIRegulationEntity(fciRegulationSymbol);
        FCIPosition fciPosition = fciPositionService.findFCIPositionById(fciRegulationSymbol, Integer.valueOf(fciPositionId));
        List<FCISpeciePosition> fciSpeciePositions = FCIPosition.getSpeciePositions(fciPosition, true);
        if (refresh) updateCurrentMarketPriceToPosition(fciPosition, true);

        List<FCISpecieType> fciSpecieTypes = fciSpecieTypeGroupService.listFCISpecieTypes();
        Set<String> fciSpecieTypesNames = fciSpecieTypes.stream().map(FCISpecieType::getName).collect(Collectors.toSet());
        Map<FCISpecieType, Double> fciRegulationCompositionBySpecieType = DomainExtractionHelper.getCompositionAsSpecieType(fciRegulation.getComposition(), fciSpecieTypes);
        Map<FCISpecieType, List<FCISpeciePosition>> groupedPositionBySpecieType = fciPositionService.groupPositionBySpecieType(fciSpeciePositions, fciSpecieTypes);

        /** Preconditions */
        analyseBiasPreconditions(fciRegulationCompositionBySpecieType, groupedPositionBySpecieType, fciSpecieTypesNames);

        Map<FCISpeciePosition, FCISpecieTypeGroup> bindings = fciSpecieTypeGroupService.listSpecieTypeGroupBindings(groupedPositionBySpecieType);

        /** Position grouping calculation */
        Map<FCISpecieType, Double> valuedPositionBySpecieType = fciPositionService.getValuedPositionBySpecieType(groupedPositionBySpecieType);
        Map<FCISpecieType, Double> percentagePositionBySpecieType = calculatePercentagePositionBySpecieType(valuedPositionBySpecieType);

        /** Regulation grouping calculation */
        Map<FCISpecieType, Double> percentageRegulationBySpecieType = DomainExtractionHelper.getCompositionAsSpecieType(fciRegulation.getComposition(), fciSpecieTypes);
        Map<FCISpecieType, Double> valuedRegulationBySpecieType = calculateValuedRegulationBySpecieType(percentageRegulationBySpecieType, valuedPositionBySpecieType);

        /** Biases grouping calculation */
        Map<FCISpecieType, Double> biasPercentagePositionBySpecieType = calculatePercentagePositionBias(percentageRegulationBySpecieType, percentagePositionBySpecieType);
        Map<FCISpecieType, Double> biasValuedPositionBySpecieType = calculateValuedPositionBias(biasPercentagePositionBySpecieType, valuedRegulationBySpecieType);

        /** Summarization */
        Double totalSummarizedPosition = valuedPositionBySpecieType.values().stream().reduce(Double::sum).orElseThrow();

        /** FCI Specie Type over Position */
        Map<FCISpecieType, Double> specieTypePercentageWeightRelativeToPosition = calculateSpecieTypePercentageOverPosition(valuedPositionBySpecieType, totalSummarizedPosition);
        Map<FCISpecieType, Double> specieTypeValueWeightRelativeToPosition = calculateSpecieTypeValueOverPosition(valuedPositionBySpecieType, totalSummarizedPosition);

        /** FCI Specie over Specie Type */
        Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToFCISpecieType = calculateSpeciePercentageOverSpecieType(fciSpeciePositions, valuedPositionBySpecieType, bindings);
        Map<FCISpeciePosition, Double> specieValueWeightRelativeToFCISpecieType = calculateSpecieValueOverSpecieType(fciSpeciePositions, valuedPositionBySpecieType, bindings);

        /** FCI Specie over Position */
        Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToPosition = calculateSpeciePercentageOverPosition(fciSpeciePositions, totalSummarizedPosition, bindings);
        Map<FCISpeciePosition, Double> specieValueWeightRelativeToPosition = calculateSpecieValueOverPosition(fciSpeciePositions, totalSummarizedPosition, bindings);

        return new RegulationLagOutcomeVO(
                biasPercentagePositionBySpecieType,
                biasValuedPositionBySpecieType,
                percentagePositionBySpecieType,
                valuedPositionBySpecieType,
                percentageRegulationBySpecieType,
                valuedRegulationBySpecieType,
                specieTypePercentageWeightRelativeToPosition,
                specieTypeValueWeightRelativeToPosition,
                speciePercentageWeightRelativeToFCISpecieType,
                specieValueWeightRelativeToFCISpecieType,
                speciePercentageWeightRelativeToPosition,
                specieValueWeightRelativeToPosition);
    }

    private List<FCISpeciePosition> updateCurrentMarketPriceToPosition(FCIPosition fciPosition, @NotNull Boolean refresh) throws Exception {
       return refresh ? fciPositionService.updateCurrentMarketPriceToPosition(fciPosition, true)
                        : fciPositionService.updateCurrentMarketPriceToPosition(fciPosition);
    }

    private Map<FCISpecieType, Double> calculateValuedRegulationBySpecieType(Map<FCISpecieType, Double> regulationPercentage,
                                                                             Map<FCISpecieType, Double> summarizedPosition) {
        Double totalPosition = CalculationServiceHelper.summarizePositionList(summarizedPosition);
        return regulationPercentage.entrySet().stream().map(entry ->
                        Map.entry(
                            entry.getKey(),
                            CalculationServiceHelper.calculatePercentageOverTotalValued(entry.getValue(), totalPosition)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public FCIPositionPercentageVO calculatePositionBiasPercentages(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception {
        AtomicInteger index = new AtomicInteger();
        Map<FCISpecieType, Double> m = calculatePositionBias(fciRegulationSymbol, fciPositionId, refresh).getPositionOverTotalPositionPercentage();
        return new FCIPositionPercentageVO(m.entrySet().stream()
                .map(e -> new FCIPercentageVO(index.getAndIncrement(), e.getKey().getName(), String.format("%.2f",e.getValue()))).collect(Collectors.toList()));
    }

    @Override
    public FCIPositionValuedVO calculatePositionBiasValued(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception {
        Map<FCISpecieType, Double> m = calculatePositionBias(fciRegulationSymbol, fciPositionId, refresh).getPositionOverTotalPositionValued();
        return new FCIPositionValuedVO(m.entrySet().stream()
                .map(e -> new FCIValuedVO(e.getKey().getName(), String.format("%.2f",e.getValue()))).collect(Collectors.toList()));
    }

    @Override
    public FCIRegulationPercentageVO calculateRegulationPercentages(String fciRegulationSymbol, String fciPositionId) throws Exception {
        AtomicInteger index = new AtomicInteger();
        Map<FCISpecieType, Double> m = calculatePositionBias(fciRegulationSymbol, fciPositionId, false).getRegulationPercentage();
        return new FCIRegulationPercentageVO(m.entrySet().stream()
                .map(e -> new FCIPercentageVO(index.getAndIncrement(), e.getKey().getName(), String.format("%.2f",e.getValue()))).collect(Collectors.toList()));
    }

    @Override
    public FCIRegulationValuedVO calculateRegulationValued(String fciRegulationSymbol, String fciPositionId) throws Exception {
        Map<FCISpecieType, Double> m = calculatePositionBias(fciRegulationSymbol, fciPositionId, false).getRegulationValued();
        return new FCIRegulationValuedVO(m.entrySet().stream()
                .map(e -> new FCIValuedVO(e.getKey().getName(), String.format("%.2f",e.getValue()))).collect(Collectors.toList()));
    }

    @Override
    public List<FCIPercentageAndValuedVO> calculatePositionBiasPercentageValued(String fciRegulationSymbol, String fciPositionId, Boolean refresh) throws Exception {
        AtomicInteger index = new AtomicInteger();
        RegulationLagOutcomeVO regulationLagOutcomeVO = calculatePositionBias(fciRegulationSymbol, fciPositionId, false);
        Map<FCISpecieType, Double> m = regulationLagOutcomeVO.getPositionOverTotalPositionPercentage();
        Map<FCISpecieType, Double> n = regulationLagOutcomeVO.getPositionOverTotalPositionValued();
        Map<FCISpecieType, Double> p = regulationLagOutcomeVO.getPositionOverRegulationBiasPercentage();
        Map<FCISpecieType, Double> q = regulationLagOutcomeVO.getPositionOverRegulationBiasValued();
        Map<FCISpecieType, Double> r = regulationLagOutcomeVO.getRegulationPercentage();
        Map<FCISpecieType, Double> s = regulationLagOutcomeVO.getRegulationValued();
        return m.entrySet().stream()
            .map(e -> new FCIPercentageAndValuedVO(index.getAndIncrement(),
                e.getKey().getName(), String.format("%.2f", e.getValue()),
                String.format("%.2f", n.get(n.keySet().stream().filter(k -> k.getFciSpecieTypeId().equals(e.getKey().getFciSpecieTypeId())).findFirst().orElseThrow())),
                String.format("%.2f", p.get(p.keySet().stream().filter(k -> k.getFciSpecieTypeId().equals(e.getKey().getFciSpecieTypeId())).findFirst().orElseThrow())),
                String.format("%.2f", q.get(q.keySet().stream().filter(k -> k.getFciSpecieTypeId().equals(e.getKey().getFciSpecieTypeId())).findFirst().orElseThrow())),
                String.format("%.2f", r.get(r.keySet().stream().filter(k -> k.getFciSpecieTypeId().equals(e.getKey().getFciSpecieTypeId())).findFirst().orElseThrow())),
                String.format("%.2f", s.get(s.keySet().stream().filter(k -> k.getFciSpecieTypeId().equals(e.getKey().getFciSpecieTypeId())).findFirst().orElseThrow()))))
                .collect(Collectors.toList());
    }


    /* Specie Type over Position */
    private Map<FCISpecieType, Double> calculateSpecieTypePercentageOverPosition(Map<FCISpecieType, Double> valuedPositionBySpecieType, Double totalSummarizedPosition) {
        return valuedPositionBySpecieType.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(),
                        entry.getValue() * totalSummarizedPosition / 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<FCISpecieType, Double> calculateSpecieTypeValueOverPosition(Map<FCISpecieType, Double> valuedPositionBySpecieType, Double totalSummarizedPosition) throws JsonProcessingException {
        return valuedPositionBySpecieType.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(),
                                (entry.getValue() * totalSummarizedPosition / 100) * totalSummarizedPosition))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /* Specie over Specie Type */
    private Map<FCISpeciePosition, Double> calculateSpeciePercentageOverSpecieType(List<FCISpeciePosition> fciSpeciePositions, Map<FCISpecieType, Double> valuedPositionBySpecieType, Map<FCISpeciePosition, FCISpecieTypeGroup> bindings) {
        return fciSpeciePositions.stream()
                .map(fciSpeciePosition -> {
                    Double fciSpecieTypeValued = valuedPositionBySpecieType.entrySet().stream()
                            .filter(vpst -> vpst.getKey().getName().equals(fciSpeciePosition.getFciSpecieType())).findFirst().orElseThrow().getValue();
                    return Map.entry(fciSpeciePosition, fciSpeciePosition.valueSpecieInPosition(bindings.get(fciSpeciePosition).getLot()) / fciSpecieTypeValued * 100);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private  Map<FCISpeciePosition, Double> calculateSpecieValueOverSpecieType(List<FCISpeciePosition> fciSpeciePositions, Map<FCISpecieType, Double> valuedPositionBySpecieType, Map<FCISpeciePosition, FCISpecieTypeGroup> bindings) {
        return fciSpeciePositions.stream()
                .map(fciSpeciePosition -> {
                    Double fciSpecieTypeValued = valuedPositionBySpecieType.entrySet().stream()
                            .filter(vpst -> vpst.getKey().getName().equals(fciSpeciePosition.getFciSpecieType())).findFirst().orElseThrow().getValue();
                    return Map.entry(fciSpeciePosition, fciSpeciePosition.valueSpecieInPosition(bindings.get(fciSpeciePosition).getLot()) / fciSpecieTypeValued);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /* Specie over Position */
    /**
     * Calculates each specie percentage relative weight over to {@link FCIPosition}
     * @param speciesInPosition List of {@link FCISpeciePosition} contained in Position
     * @param  totalSummarizedPosition Represents total valued position as sum of individual valued contained species
     * @return A List of species with their relative percentage over position
     */
    private Map<FCISpeciePosition, Double> calculateSpeciePercentageOverPosition(List<FCISpeciePosition> speciesInPosition, Double totalSummarizedPosition, Map<FCISpeciePosition, FCISpecieTypeGroup> bindings) {
        return speciesInPosition.stream()
                .map(fciSpeciePosition -> Map.entry(fciSpeciePosition,
                        fciSpeciePosition.valueSpecieInPosition(bindings.get(fciSpeciePosition).getLot())
                            * totalSummarizedPosition / 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Calculates each specie value relative weight over to {@link FCIPosition}
     * @param speciesInPosition List of {@link FCISpeciePosition} contained in Position
     * @param  totalSummarizedPosition Represents total valued position as sum of individual valued contained species
     * @return A List of species with their relative percentage over position
     */
    private Map<FCISpeciePosition, Double> calculateSpecieValueOverPosition(List<FCISpeciePosition> speciesInPosition, Double totalSummarizedPosition, Map<FCISpeciePosition, FCISpecieTypeGroup> bindings) {
        return speciesInPosition.stream()
                .map(fciSpeciePosition -> Map.entry(fciSpeciePosition,
                                (fciSpeciePosition.valueSpecieInPosition(bindings.get(fciSpeciePosition).getLot()) * totalSummarizedPosition / 100) * totalSummarizedPosition))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Calculates Percentage Position biases for each Position {@link FCISpecieType}
     * @param percentageRegulationBySpecieType Defined percentages for {@link FCIRegulation}
     * @param percentagePositionBySpecieType Percentage {@link FCIPosition} grouped by {@link FCISpecieType}
     * @return Differences among FCIRegulation and FCIPosition composition specie types
     */
    private Map<FCISpecieType, Double> calculatePercentagePositionBias(
            Map<FCISpecieType, Double> percentageRegulationBySpecieType,
            Map<FCISpecieType, Double> percentagePositionBySpecieType) {
        return percentagePositionBySpecieType.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() - percentageRegulationBySpecieType.get(entry.getKey())))
                .   collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Calculates Value Position biases for each Position {@link FCISpecieType}
     * @param biasPercentagePositionBySpecieType Defined percentages for {@link FCIRegulation}
     * @param valuedRegulationBySpecieType Valued {@link FCIPosition} grouped by {@link FCISpecieType}
     * @return Differences among FCIRegulation and FCIPosition composition specie types
     */
    private Map<FCISpecieType, Double> calculateValuedPositionBias(Map<FCISpecieType, Double> biasPercentagePositionBySpecieType,
                                                                   Map<FCISpecieType, Double> valuedRegulationBySpecieType) {
        Double totalSummarizedPosition = valuedRegulationBySpecieType.values().stream().reduce(Double::sum).orElseThrow();
        return biasPercentagePositionBySpecieType.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() * totalSummarizedPosition / 100))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Calculates Percentage of {@link FCISpecieType} related to total valued Position
     * @param valuedPositionBySpecieType Indicates each position {@link FCISpecieType} value
     */
    private Map<FCISpecieType, Double> calculatePercentagePositionBySpecieType(Map<FCISpecieType, Double> valuedPositionBySpecieType) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(valuedPositionBySpecieType);
        return calculatePercentageBySpecieType(valuedPositionBySpecieType, totalValuedPosition);
    }

    private Map<FCISpecieType, Double> calculateValuedPosition(Map<FCISpecieType, Double> summarizedPosition) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(summarizedPosition);
        return calculatePercentageBySpecieType(summarizedPosition, totalValuedPosition);
    }


    private Map<FCISpecieType, Double> calculatePercentageBySpecieType(Map<FCISpecieType, Double> summarizedPosition, Double totalValuedPosition) {
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
    private void analyseBiasPreconditions(Map<FCISpecieType, Double> fciRegulationComposition,
                                          Map<FCISpecieType, List<FCISpeciePosition>> fciSpecieTypePosition,
                                          Set<String> fciSpecieTypesNames) {
        Double percentageSumReduction = fciRegulationComposition.values().stream().reduce(Double::sum).orElseThrow();

        /** fciRegulationComposition must close to 100% */
        if (!Constants.TOTAL_PERCENTAGE.equals(percentageSumReduction)) {
            throw new FailedValidationException(ExceptionMessage.TOTAL_PERCENTAGE.msg);
        }

        /** All Specie type indicated in FCI Regulation Percentage composition are expected to be received for processing */
        fciRegulationComposition.keySet().forEach(fciRegulationSpecieType -> {
            if (fciSpecieTypePosition.keySet().stream().noneMatch(fciSpecieType -> fciSpecieTypesNames.contains(fciSpecieType.getName()))) {
                throw new IllegalArgumentException(String.format(ExceptionMessage.REGULATION_SPECIE_TYPE_DOES_NOT_MATCH.msg, fciRegulationSpecieType));
            }
        });
    }

}
