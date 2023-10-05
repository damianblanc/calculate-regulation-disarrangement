package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.*;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advices Price Uniform Distribution criteria implementation
 */
@Service
public class FCIPositionCriteriaPriceUniformDistributionService implements FCIPositionAdvisorService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private FCICalculationService fciCalculationService;
    @Autowired
    private FCIPositionService fciPositionService;
    @Autowired
    private BymaHttpService bymaHttpService;
    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;

    @Override
    public OperationAdviceVerboseVO advice(FCIPosition fciPosition) throws JsonProcessingException {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();

        ImmutableMap<SpecieType, Function<SpecieData, Multimap<SpecieType, OperationAdviceVO>>> specieTypeProcess =
                ImmutableMap.<SpecieType, Function<SpecieData, Multimap<SpecieType, OperationAdviceVO>>>builder()
                .put(SpecieType.BOND, this::bond)
                .put(SpecieType.EQUITY, this::equity)
                        .put(SpecieType.CASH, this::cash)
                .build();

        PriceUniformlyDistributionCriteriaParameterDTO parameters =
                fciPositionAdviceService.getParameters(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);

        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionDisarrangement(fciPosition);
        Map<SpecieType, Double> percentagePosition = regulationLagOutcomeVO.getRegulationLags();

        percentagePosition.forEach((specieType, speciePercentage) ->
            specieTypeAdvices.putAll(specieTypeProcess.get(specieType).apply(
                    SpecieData.builder()
                            .orderType(Objects.requireNonNull(OrderType.valueOfSign(speciePercentage)))
                            .fciPositionList(CalculationServiceHelper.getFciPositionListFilteredBySpecieType(fciPosition.getFciPositionList(), specieType))
                            .parameters(parameters).speciePercentage(speciePercentage)
                            .regulationSpeciePercentage(regulationLagOutcomeVO.getRegulationPercentage().get(specieType))
                            .regulationSpecieValued(regulationLagOutcomeVO.getRegulationValued().get(specieType))
                            .summarizedPosition(CalculationServiceHelper.summarizePositionList(regulationLagOutcomeVO.getRegulationValued()))
                            .build())));

        return OperationAdviceVerboseVO.builder()
                .fciRegulationComposition(fciPosition.getFciRegulationDTO().getComposition())
                .regulationLagOutcomeVO(regulationLagOutcomeVO)
                .operationAdviceVO(specieTypeAdvices.asMap())
                .build();
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(FCIPosition fciPosition) throws JsonProcessingException {
        return advice(fciPosition);
    }

    private Multimap<SpecieType, OperationAdviceVO> equity(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        bymaHttpService.getEquityOrderByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                            OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                                    calculatePercentageOverPriceToCoverValued(
                                            specieData.getSummarizedPosition(),
                                            specieData.getFciPositionList(),
                                            specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                                    e.getSymbol(), e.getTrade(), specieData.getOrderType().getOperationAdvice());
                            specieTypeAdvices.put(SpecieType.EQUITY, operationAdviceVO);
                        }
                );
        return specieTypeAdvices;
    }


    private Multimap<SpecieType, OperationAdviceVO> bond(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        Supplier<Stream<BymaBondResponse.BymaBondResponseElement>> limitedBonds = supplyLimitedBonds(specieData);
        Set<BymaBondResponse.BymaBondResponseElement> canAdviceBonds = limitedBonds.get().filter(e ->
                setSpecieTypeAdvice(
                        calculatePercentageOverPriceToCoverValued(
                                specieData.getSummarizedPosition(),
                                specieData.getFciPositionList(),
                                specieData.getSpeciePercentage(),
                                CalculationServiceHelper.calculateMinNumber(
                                        specieData.getFciPositionList().size(),
                                        specieData.getParameters().getElementQuantity())),
                        e.getSymbol(), e.getPrice(), specieData.getOrderType().getOperationAdvice()).canAdvice()).collect(Collectors.toSet());

        canAdviceBonds.forEach(e -> {
                    OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                        calculatePercentageOverPriceToCoverValued(
                                specieData.getSummarizedPosition(),
                                specieData.getFciPositionList(),
                                specieData.getSpeciePercentage(),
                                CalculationServiceHelper.calculateMinNumber(
                                        canAdviceBonds.size(),
                                        specieData.getParameters().getElementQuantity())),
                        e.getSymbol(), e.getPrice(), specieData.getOrderType().getOperationAdvice());
                        specieTypeAdvices.put(SpecieType.BOND, operationAdviceVO);
                    }
                );
        return specieTypeAdvices;
    }

    @NotNull
    private Supplier<Stream<BymaBondResponse.BymaBondResponseElement>> supplyLimitedBonds(SpecieData specieData) {
        return () -> bymaHttpService.getBondsOrderByPriceFilteredBySpecieList(specieData.getOrderType(),
                        specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity());
    }

    private Multimap<SpecieType, OperationAdviceVO> cash(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        Double valuedAdvice = Math.abs(CalculationServiceHelper.calculatePercentageOverTotalValued(specieData.getSpeciePercentage(), specieData.summarizedPosition));
//        Double valuedAdvice = calculatePercentageOverPriceToCoverValued(
//                specieData.getSummarizedPosition(),
//                specieData.getFciPositionList(),
//                specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity());
        OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(valuedAdvice,
                SpecieType.CASH.name(), String.valueOf(valuedAdvice), specieData.getOrderType().getOperationAdvice());
        specieTypeAdvices.put(SpecieType.CASH, operationAdviceVO);
        return specieTypeAdvices;
    }

    private static OperationAdviceVO setSpecieTypeAdvice(Double percentageOverPriceToCoverValued, String symbol, String price,
                                            OperationAdvice operationAdvice) {
        return new OperationAdviceVO(symbol, operationAdvice,
                CalculationServiceHelper.calculateSpecieQuantityToCover(percentageOverPriceToCoverValued, Double.valueOf(price)),
                Double.valueOf(price));
    }

    @NotNull
    private Double calculatePercentageOverPriceToCoverValued(Double summarizedPosition, List<SpeciePosition> fciPositionList, Double speciePercentage, Integer elementQuantity) {
        Double priceToCover = CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, summarizedPosition);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(elementQuantity);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    @Data
    @Builder
    private static class SpecieData {
        private OrderType orderType;
        private List<SpeciePosition> fciPositionList;
        private PriceUniformlyDistributionCriteriaParameterDTO parameters;
        private Double speciePercentage;
        private Double regulationSpeciePercentage;
        private Double regulationSpecieValued;
        private Double summarizedPosition;
    }
}
