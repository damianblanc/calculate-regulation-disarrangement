package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.*;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.*;
import com.bymatech.calculateregulationdisarrangement.util.CalculationServiceHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FCIPositionAdvisorPriceUniformlyDistributionService implements FCIPositionAdvisorService {

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

        PriceUniformlyDistributionCriteriaParameterDTO parameters =
                fciPositionAdviceService.getParameters(AdviceCalculationCriteria.PRICE_UNIFORMLY_DISTRIBUTION);

        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionDisarrangement(fciPosition);
        Map<SpecieType, Double> percentagePosition = regulationLagOutcomeVO.getPercentagePosition();
        percentagePosition.forEach((specieType, speciePercentage) -> {
            if (SpecieType.BOND == specieType) {
                OrderType orderType = Objects.requireNonNull(OrderType.valueOfSign(speciePercentage));
                bymaHttpService.getBondsOrderByPriceFilteredBySpecieList(orderType, fciPosition.getFciPositionList())
                        .stream().limit(parameters.getElementQuantity()).forEach(e ->
                        setSpecieTypeAdvice(calculatePercentageOverPriceToCoverValued(fciPosition, speciePercentage, parameters.getElementQuantity()),
                                e, orderType.getOperationAdvice(), specieTypeAdvices));
            }
        });
        return specieTypeAdvices.asMap();
    }

    private static void setSpecieTypeAdvice(Double percentageOverPriceToCoverValued, BymaBondResponse.BymaBondResponseElement e,
                                            OperationAdvice operationAdvice, Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices) {
        Integer quantityToCover = CalculationServiceHelper.calculateSpecieQuantityToCover(percentageOverPriceToCoverValued, Double.valueOf(e.getPrice()));
        OperationAdviceVO operationAdviceVO = new OperationAdviceVO(e.getDescription(), operationAdvice, quantityToCover);
        specieTypeAdvices.put(SpecieType.BOND, operationAdviceVO);
    }

    @NotNull
    private Double calculatePercentageOverPriceToCoverValued(FCIPosition fciPosition, Double speciePercentage, Integer elementQuantity) {
        Double priceToCover = calculatePriceToCover(fciPosition, speciePercentage);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(elementQuantity);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    private Double calculatePriceToCover(FCIPosition fciPosition, Double speciePercentage) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(fciPosition.getFciPositionList());
        return CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, totalValuedPosition);
    }
}
