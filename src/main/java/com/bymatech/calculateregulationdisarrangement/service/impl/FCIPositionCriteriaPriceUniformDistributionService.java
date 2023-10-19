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
    private BymaHttpService bymaHttpService;
    @Autowired
    private FCIPositionAdviceService fciPositionAdviceService;
    @Autowired
    private FCIRegulationCRUDService fciRegulationCRUDService;

    @Override
    public OperationAdviceVerboseVO advice(String symbol, FCIPosition fciPosition) throws JsonProcessingException {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        FCIRegulation fciRegulation = fciRegulationCRUDService.findFCIRegulation(symbol);

        ImmutableMap<SpecieType, Function<SpecieData, Multimap<SpecieType, OperationAdviceVO>>> specieTypeProcess =
                ImmutableMap.<SpecieType, Function<SpecieData, Multimap<SpecieType, OperationAdviceVO>>>builder()
                .put(SpecieType.Bond, this::bond)
                .put(SpecieType.Equity, this::equity)
                        .put(SpecieType.Cash, this::cash)
                .build();

        PriceUniformlyDistributionCriteriaParameterDTO parameters =
                fciPositionAdviceService.getParameters(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);

        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionDisarrangement(symbol, fciPosition);
        Map<SpecieType, Double> percentagePosition = regulationLagOutcomeVO.getRegulationLags();

        percentagePosition.forEach((specieType, speciePercentage) ->
            specieTypeAdvices.putAll(specieTypeProcess.get(specieType).apply(
                    SpecieData.builder()
                            .orderType(Objects.requireNonNull(OrderType.valueOfSign(speciePercentage)))
                            .fciPositionList(CalculationServiceHelper.getFciPositionListFilteredBySpecieType(fciPosition.getPosition(), specieType))
                            .parameters(parameters).speciePercentage(speciePercentage)
                            .regulationSpeciePercentage(regulationLagOutcomeVO.getRegulationPercentage().get(specieType))
                            .regulationSpecieValued(regulationLagOutcomeVO.getRegulationValued().get(specieType))
                            .summarizedPosition(CalculationServiceHelper.summarizePositionList(regulationLagOutcomeVO.getRegulationValued()))
                            .build())));

        AtomicInteger index = new AtomicInteger();
        List<OperationAdviceSpecieType> operationAdviceSpecieTypes = specieTypeAdvices.asMap().entrySet().stream()
                .map(e -> new OperationAdviceSpecieType(index.getAndIncrement(), e.getKey().name(), e.getValue())).toList();

        return OperationAdviceVerboseVO.builder()
                .fciRegulationComposition(fciRegulation.getComposition())
                .regulationLagOutcomeVO(regulationLagOutcomeVO)
                .operationAdvicesVO(operationAdviceSpecieTypes)
                .build();
    }

    @Override
    public OperationAdviceVerboseVO adviceVerbose(String symbol, FCIPosition fciPosition) throws JsonProcessingException {
        return advice(symbol, fciPosition);
    }

    private Multimap<SpecieType, OperationAdviceVO> equity(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        bymaHttpService.getEquityOrderByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                            OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                                    index,
                                    calculatePercentageOverPriceToCoverValued(
                                            specieData.getSummarizedPosition(),
                                            specieData.getFciPositionList(),
                                            specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                                    e.getSymbol(), e.getTrade(), specieData.getOrderType().getOperationAdvice());
                            specieTypeAdvices.put(SpecieType.Equity, operationAdviceVO);
                        }
                );
        return specieTypeAdvices;
    }


    private Multimap<SpecieType, OperationAdviceVO> bond(SpecieData specieData) {
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        Supplier<Stream<BymaBondResponse.BymaBondResponseElement>> limitedBonds = supplyLimitedBonds(specieData);
        Set<BymaBondResponse.BymaBondResponseElement> canAdviceBonds = limitedBonds.get().filter(e ->
                setSpecieTypeAdvice(
                        index,
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
                        index,
                        calculatePercentageOverPriceToCoverValued(
                                specieData.getSummarizedPosition(),
                                specieData.getFciPositionList(),
                                specieData.getSpeciePercentage(),
                                CalculationServiceHelper.calculateMinNumber(
                                        canAdviceBonds.size(),
                                        specieData.getParameters().getElementQuantity())),
                        e.getSymbol(), e.getPrice(), specieData.getOrderType().getOperationAdvice());
                        specieTypeAdvices.put(SpecieType.Bond, operationAdviceVO);
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
        AtomicInteger index = new AtomicInteger();
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
            Double valuedAdvice = Math.abs(CalculationServiceHelper.calculatePercentageOverTotalValued(specieData.getSpeciePercentage(), specieData.summarizedPosition));
//        Double valuedAdvice = calculatePercentageOverPriceToCoverValued(
//                specieData.getSummarizedPosition(),
//                specieData.getFciPositionList(),
//                specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity());
        OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(index, valuedAdvice,
                SpecieType.Cash.name(), String.valueOf(valuedAdvice), specieData.getOrderType().getOperationAdvice());
        specieTypeAdvices.put(SpecieType.Cash, operationAdviceVO);
        return specieTypeAdvices;
    }

    private static OperationAdviceVO setSpecieTypeAdvice(AtomicInteger index, Double percentageOverPriceToCoverValued, String symbol, String price,
                                            OperationAdvice operationAdvice) {
        return new OperationAdviceVO(index.getAndIncrement(), symbol, operationAdvice,
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
