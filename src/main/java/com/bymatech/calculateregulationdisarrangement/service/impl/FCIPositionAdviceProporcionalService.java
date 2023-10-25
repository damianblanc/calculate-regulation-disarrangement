package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import com.bymatech.calculateregulationdisarrangement.service.FCIPositionAdvisorService;
import com.bymatech.calculateregulationdisarrangement.service.FCIRegulationCRUDService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class FCIPositionAdviceProporcionalService implements FCIPositionAdvisorService {

    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;

    @Autowired
    private FCICalculationService fciCalculationService;

    @Override
    public OperationAdviceVerboseVO advice(String fciRegulationSymbol, String fciPositionId) throws Exception {
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        FCIRegulation fciRegulation = fciRegulationCRUDService.findFCIRegulation(fciRegulationSymbol);

        Set<FCIComposition> composition = fciRegulation.getComposition();

//        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionBias(fciRegulationSymbol, fciPositionId);
//        Map<FCISpecieType, Double> percentagePosition = regulationLagOutcomeVO.getRegulationLags();



//        ImmutableMap<SpecieType, Function<FCIPositionCriteriaPriceUniformDistributionService.SpecieData, Multimap<SpecieType, OperationAdviceVO>>> specieTypeProcess =
//                ImmutableMap.<SpecieType, Function<FCIPositionCriteriaPriceUniformDistributionService.SpecieData, Multimap<SpecieType, OperationAdviceVO>>>builder()
//                        .put(SpecieType.Bond, this::bond)
//                        .put(SpecieType.Equity, this::equity)
//                        .put(SpecieType.Cash, this::cash)
//                        .build();
//
//        PriceUniformlyDistributionCriteriaParameterDTO parameters =
//                fciPositionAdviceService.getParameters(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);


//        percentagePosition.forEach((specieType, speciePercentage) ->
//                specieTypeAdvices.putAll(specieTypeProcess.get(specieType).apply(
//                        FCIPositionCriteriaPriceUniformDistributionService.SpecieData.builder()
//                                .orderType(Objects.requireNonNull(OrderType.valueOfSign(speciePercentage)))
//                                .fciPositionList(CalculationServiceHelper.getFciPositionListFilteredBySpecieType(fciPosition.getPosition(), specieType))
//                                .parameters(parameters).speciePercentage(speciePercentage)
//                                .regulationSpeciePercentage(regulationLagOutcomeVO.getRegulationPercentage().get(specieType))
//                                .regulationSpecieValued(regulationLagOutcomeVO.getRegulationValued().get(specieType))
//                                .summarizedPosition(CalculationServiceHelper.summarizePositionList(regulationLagOutcomeVO.getRegulationValued()))
//                                .build())));
//
//        AtomicInteger index = new AtomicInteger();
//        List<OperationAdviceSpecieType> operationAdviceSpecieTypes = specieTypeAdvices.asMap().entrySet().stream()
//                .map(e -> new OperationAdviceSpecieType(index.getAndIncrement(), e.getKey().name(), e.getValue())).toList();

        return OperationAdviceVerboseVO.builder()
                .fciRegulationComposition(fciRegulation.getComposition())
//                .regulationLagOutcomeVO(regulationLagOutcomeVO)
//                .operationAdvicesVO(operationAdviceSpecieTypes)
                .build();
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String fciRegulationSymbol, String fciPositionId) throws Exception {
        return null;
    }
}
