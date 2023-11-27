package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.*;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
import com.bymatech.calculateregulationdisarrangement.util.DomainExtractionHelper;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FCIPositionAdviceProportionalService implements FCIPositionAdvisorService {

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;

    @Autowired
    private FCICalculationService fciCalculationService;

    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Autowired
    private FCIPositionService fciPositionService;

    @Autowired
    private FCISpecieTypeGroupService fciSpecieTypeGroupService;

    @Override
    public List<OperationAdviceSpecieType> advice(String fciRegulationSymbol, String fciPositionId) throws Exception {
        AtomicInteger index = new AtomicInteger();
        List<OperationAdviceSpecieType> specieTypeAdvices = new ArrayList<>();
        FCIRegulation fciRegulation = fciRegulationCRUDService.findFCIRegulation(fciRegulationSymbol);
        FCIPosition fciPosition = fciRegulation.getPositions().stream().filter(p -> p.getId().equals(Integer.valueOf(fciPositionId))).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ExceptionMessage.FCI_POSITION_ENTITY_NOT_FOUND.msg, fciRegulationSymbol, fciPositionId)));
        List<FCISpeciePosition> fciSpeciesInPosition = FCIPosition.getSpeciePositions(fciPosition, true);
        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionBias(fciRegulationSymbol, fciPositionId, false);

        Map<FCISpecieType, List<OperationAdviceVO>> advices = doAdvice(regulationLagOutcomeVO, fciSpeciesInPosition);
        advices.forEach((key, value) -> specieTypeAdvices.add(new OperationAdviceSpecieType(index.getAndIncrement(), key.getName(), value)));
            return specieTypeAdvices;
    }

    private Map<FCISpecieType, List<OperationAdviceVO>> doAdvice(RegulationLagOutcomeVO regulationLagOutcomeVO, List<FCISpeciePosition> fciSpeciesInPosition) {
        AtomicInteger index = new AtomicInteger();
        Map<FCISpecieType, List<FCISpeciePosition>> groupedSpeciesBySpecieTypes =
                fciPositionService.groupPositionBySpecieType(fciSpeciesInPosition, fciSpecieTypeGroupService.listFCISpecieTypes());

        return groupedSpeciesBySpecieTypes.entrySet().stream().map(entry ->
                Map.entry(entry.getKey(),
                    entry.getValue().stream().map(fciSpeciePosition -> {
                        Double fciSpecieTypeBiasOverRegulationPercentage =
                                DomainExtractionHelper.findFciSpecieType(regulationLagOutcomeVO.getPositionOverRegulationBiasPercentage(), fciSpeciePosition.getFciSpecieType()).getValue();
                        Double fciSpecieTypeBiasOverRegulationValued =
                                DomainExtractionHelper.findFciSpecieType(regulationLagOutcomeVO.getPositionOverRegulationBiasValued(), fciSpeciePosition.getFciSpecieType()).getValue();
                        Double fciSpecieOverSpecieTypeValued =
                                DomainExtractionHelper.findFciSpeciePosition(regulationLagOutcomeVO.getSpecieValueWeightRelativeToFCISpecieType(), fciSpeciePosition).getValue();

                        Double fciSpecieQuantity =  Math.abs(fciSpecieOverSpecieTypeValued * fciSpecieTypeBiasOverRegulationValued / fciSpeciePosition.getCurrentMarketPrice());

                        return new OperationAdviceVO(index.getAndIncrement(), fciSpeciePosition.getSymbol(), OperationAdvice.getOperationAdvice(fciSpecieTypeBiasOverRegulationPercentage),
                                fciSpecieQuantity, fciSpeciePosition.getCurrentMarketPrice());
                }).toList()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @NotNull
    private Double calculatePercentageOverPriceToCoverValued(Double summarizedPosition, List<FCISpeciePosition> fciPositionList, Double speciePercentage, Integer elementQuantity) {
        Double priceToCover = CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, summarizedPosition);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(elementQuantity);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String fciRegulationSymbol, String fciPositionId) throws Exception {
        return null;
    }

    @Data
    @Builder
    private static class SpecieTypeAdvice {
        private OrderType orderType;
        private List<FCISpeciePosition> fciPositionList;
        private PriceUniformlyDistributionCriteriaParameterDTO parameters;
        private Double speciePercentage;
        private Double regulationSpeciePercentage;
        private Double regulationSpecieValued;
        private Double summarizedPosition;
    }
}