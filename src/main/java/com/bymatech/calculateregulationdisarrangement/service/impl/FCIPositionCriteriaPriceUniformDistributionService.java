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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

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
    public Map<SpecieType, Collection<OperationAdviceVO>> advice(FCIPosition fciPosition) throws JsonProcessingException {
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
        Map<SpecieType, Double> percentagePosition = regulationLagOutcomeVO.getPercentagePosition();
        percentagePosition.forEach((specieType, speciePercentage) ->
            specieTypeAdvices.putAll(specieTypeProcess.get(specieType).apply(
                    SpecieData.builder().orderType(Objects.requireNonNull(OrderType.valueOfSign(speciePercentage)))
                            .fciPositionList(fciPosition.getFciPositionList())
                            .parameters(parameters).speciePercentage(speciePercentage).build())));
        return specieTypeAdvices.asMap();
    }

    private Multimap<SpecieType, OperationAdviceVO> equity(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        bymaHttpService.getEquityOrderByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                            OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                                    calculatePercentageOverPriceToCoverValued(specieData.getFciPositionList(),
                                            specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                                    e.getSymbol(), e.getTrade(), specieData.getOrderType().getOperationAdvice());
                            specieTypeAdvices.put(SpecieType.EQUITY, operationAdviceVO);
                        }
                );
        return specieTypeAdvices;
    }


    private Multimap<SpecieType, OperationAdviceVO> bond(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        bymaHttpService.getBondsOrderByPriceFilteredBySpecieList(specieData.getOrderType(), specieData.getFciPositionList())
                .stream().limit(specieData.getParameters().getElementQuantity()).forEach(e -> {
                        OperationAdviceVO operationAdviceVO = setSpecieTypeAdvice(
                                calculatePercentageOverPriceToCoverValued(specieData.getFciPositionList(),
                                        specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity()),
                                e.getSymbol(), e.getPrice(), specieData.getOrderType().getOperationAdvice());
                        specieTypeAdvices.put(SpecieType.BOND, operationAdviceVO);
                    }
                );
        return specieTypeAdvices;
    }

    private Multimap<SpecieType, OperationAdviceVO> cash(SpecieData specieData) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();
        Double valuedAdvice = calculatePercentageOverPriceToCoverValued(specieData.getFciPositionList(),
                specieData.getSpeciePercentage(), specieData.getParameters().getElementQuantity());
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
    private Double calculatePercentageOverPriceToCoverValued(List<SpeciePosition> fciPositionList, Double speciePercentage, Integer elementQuantity) {
        Double priceToCover = calculatePriceToCover(fciPositionList, speciePercentage);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(elementQuantity);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    private Double calculatePriceToCover(List<SpeciePosition> fciPositionList, Double speciePercentage) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(fciPositionList);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, totalValuedPosition);
    }

    @Data
    @Builder
    private static class SpecieData {
        private OrderType orderType;
        private List<SpeciePosition> fciPositionList;
        private PriceUniformlyDistributionCriteriaParameterDTO parameters;
        private Double speciePercentage;
    }
}
