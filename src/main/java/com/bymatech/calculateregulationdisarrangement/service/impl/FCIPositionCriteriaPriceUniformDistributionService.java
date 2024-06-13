package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.*;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
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
import java.util.concurrent.atomic.AtomicInteger;
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
    private MarketHttpService marketHttpService;
    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;
    @Autowired
    private FCIRegulationService fciRegulationService;

    @Override
    public List<OperationAdviceSpecieType> advice(String fciRegulationSymbol, String fciPositionId) throws Exception {
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        fciRegulationService.findFCIRegulationEntity(fciRegulationSymbol);

        ImmutableMap<SpecieTypeGroupEnum, Function<SpecieData, Multimap<SpecieTypeGroupEnum, OperationAdviceVO>>> specieTypeProcess =
                ImmutableMap.<SpecieTypeGroupEnum, Function<SpecieData, Multimap<SpecieTypeGroupEnum, OperationAdviceVO>>>builder()
                .put(SpecieTypeGroupEnum.Bond, this::bond)
                .put(SpecieTypeGroupEnum.Equity, this::equity)
                    .put(SpecieTypeGroupEnum.Cedears, this::cedears)
                        .put(SpecieTypeGroupEnum.Cash, this::cash)
                .build();

        PriceUniformlyDistributionCriteriaParameterDTO parameters =
                fciPositionAdviceService.getAdviceParameters(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);

        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionBias(fciRegulationSymbol, fciPositionId, false);
        Map<FCISpecieType, Double> percentagePosition = regulationLagOutcomeVO.getPositionOverRegulationBiasPercentage();

        percentagePosition.forEach((specieType, speciePercentage) ->
            specieTypeAdvices.putAll(specieTypeProcess.get(specieType).apply(
                    SpecieData.builder()
                            .orderType(Objects.requireNonNull(OrderType.valueOfSign(speciePercentage)))
//                            .fciPositionList(CalculationServiceHelper.getFciPositionListFilteredBySpecieType(fciPosition.getPosition(), specieType))
                            .parameters(parameters).speciePercentage(speciePercentage)
                            .regulationSpeciePercentage(regulationLagOutcomeVO.getRegulationPercentage().get(specieType))
                            .regulationSpecieValued(regulationLagOutcomeVO.getRegulationValued().get(specieType))
                            .summarizedPosition(CalculationServiceHelper.summarizePositionList(regulationLagOutcomeVO.getRegulationValued()))
                            .build())));

        AtomicInteger index = new AtomicInteger();
//        List<OperationAdviceSpecieType> operationAdviceSpecieTypes = specieTypeAdvices.asMap().entrySet().stream()
//                .map(e -> new OperationAdviceSpecieType(index.getAndIncrement(), e.getKey().name(), e.getValue())).toList();

//        return OperationAdviceVerboseVO.builder()
//                .fciRegulationComposition(fciRegulation.getComposition())
//                .regulationLagOutcomeVO(regulationLagOutcomeVO)
//                .operationAdvicesVO(operationAdviceSpecieTypes)
//                .build();
        return null;
    }

    private Multimap<SpecieTypeGroupEnum, OperationAdviceVO> cedears(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        marketHttpService.getEquityOrderedByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
            .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                    OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                        index,
                        calculatePercentageOverPriceToCoverValued(
                            specieData.getSummarizedPosition(),
                            specieData.getFciPositionList(),
                            specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                        e.getMarketSymbol(), e.getMarketPrice(), specieData.getOrderType().getOperationAdvice());
                    specieTypeAdvices.put(SpecieTypeGroupEnum.Equity, operationAdviceVO);
                }
            );
        return specieTypeAdvices;
    }

    @Override
    public List<OperationAdviceSpecieTypeFlatFormat> adviceFlatFormat(String fciRegulationSymbol,
        String fciPositionId) throws Exception {
        return null;
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String fciRegulationSymbol, String fciPositionId) throws Exception {
//        return advice(fciRegulationSymbol, fciPositionId);
        return null;
    }

    private Multimap<SpecieTypeGroupEnum, OperationAdviceVO> equity(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        marketHttpService.getEquityOrderedByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                            OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                                    index,
                                    calculatePercentageOverPriceToCoverValued(
                                            specieData.getSummarizedPosition(),
                                             specieData.getFciPositionList(),
                                            specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                                    e.getMarketSymbol(), e.getMarketPrice(), specieData.getOrderType().getOperationAdvice());
                            specieTypeAdvices.put(SpecieTypeGroupEnum.Equity, operationAdviceVO);
                        }
                );
        return specieTypeAdvices;
    }


    private Multimap<SpecieTypeGroupEnum, OperationAdviceVO> bond(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        Supplier<Stream<MarketBondResponse.MarketBondResponseElement>> limitedBonds = supplyLimitedBonds(specieData);
        Set<MarketBondResponse.MarketBondResponseElement> canAdviceBonds = limitedBonds.get().filter(e ->
                setSpecieTypeAdvice(
                        index,
                        calculatePercentageOverPriceToCoverValued(
                                specieData.getSummarizedPosition(),
                                specieData.getFciPositionList(),
                                specieData.getSpeciePercentage(),
                                CalculationServiceHelper.calculateMinNumber(
                                        specieData.getFciPositionList().size(),
                                        specieData.getParameters().getElementQuantity())),
                        e.getMarketSymbol(), e.getMarketPrice(), specieData.getOrderType().getOperationAdvice()).canAdvice()).collect(Collectors.toSet());

        canAdviceBonds.forEach(e -> {
                    OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                        index,
                        calculatePercentageOverPriceToCoverValued(
                                specieData.getSummarizedPosition(),
                                specieData.getFciPositionList(),
                                specieData.getSpeciePercentage(),
                                CalculationServiceHelper.calculateMinNumber(
                                        canAdviceBonds.size(),
                                        specieData.getParameters().getElementQuantity())),
                        e.getMarketSymbol(), e.getMarketPrice(), specieData.getOrderType().getOperationAdvice());
                        specieTypeAdvices.put(SpecieTypeGroupEnum.Bond, operationAdviceVO);
                    }
                );
        return specieTypeAdvices;
    }

    @NotNull
    private Supplier<Stream<MarketBondResponse.MarketBondResponseElement>> supplyLimitedBonds(SpecieData specieData) {
//        return () -> marketHttpService.getBondsOrderByPriceFilteredBySpecieList(specieData.getOrderType(),
//                        specieData.getFciPositionList())
//                .stream().limit(specieData.getParameters().getElementQuantity());
        return null;
    }

    private Multimap<SpecieTypeGroupEnum, OperationAdviceVO> cash(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieTypeGroupEnum, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
            Double valuedAdvice = Math.abs(CalculationServiceHelper.calculatePercentageOverTotalValued(specieData.getSpeciePercentage(), specieData.summarizedPosition));
//        Double valuedAdvice = calculatePercentageOverPriceToCoverValued(
//                specieData.getSummarizedPosition(),
//                specieData.getFciPositionList(),
//                specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity());
        OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(index, valuedAdvice,
                SpecieTypeGroupEnum.Cash.name(), String.valueOf(valuedAdvice), specieData.getOrderType().getOperationAdvice());
        specieTypeAdvices.put(SpecieTypeGroupEnum.Cash, operationAdviceVO);
        return specieTypeAdvices;
    }

    private static OperationAdviceVO setSpecieTypeAdvice(AtomicInteger index, Double percentageOverPriceToCoverValued, String symbol, String price,
                                            OperationAdvice operationAdvice) {
        return new OperationAdviceVO(index.getAndIncrement(), symbol, operationAdvice,
                CalculationServiceHelper.calculateSpecieQuantityToCover(percentageOverPriceToCoverValued, Double.parseDouble(price)).doubleValue(),
                Double.parseDouble(price), Double.parseDouble(price) * Double.parseDouble(price));
    }

    @NotNull
    private Double calculatePercentageOverPriceToCoverValued(Double summarizedPosition, List<FCISpeciePosition> fciPositionList, Double speciePercentage, Integer elementQuantity) {
        Double priceToCover = CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, summarizedPosition);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(elementQuantity);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    @Data
    @Builder
    private static class SpecieData {
        private OrderType orderType;
        private List<FCISpeciePosition> fciPositionList;
        private PriceUniformlyDistributionCriteriaParameterDTO parameters;
        private Double speciePercentage;
        private Double regulationSpeciePercentage;
        private Double regulationSpecieValued;
        private Double summarizedPosition;
    }
}
