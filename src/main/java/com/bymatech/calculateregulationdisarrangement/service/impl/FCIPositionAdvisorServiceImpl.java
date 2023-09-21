package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.AdviceCalculationCriteria;
import com.bymatech.calculateregulationdisarrangement.domain.OperationAdvice;
import com.bymatech.calculateregulationdisarrangement.domain.OrderType;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FCIPositionAdvisorServiceImpl implements FCIPositionAdvisorService {

    @Autowired
    private FCICalculationService fciCalculationService;

    @Autowired
    private FCIPositionService fciPositionService;

    @Autowired
    private BymaService bymaService;

    @Override
    public Map<SpecieType, Collection<OperationAdviceVO>> advicePositionByPrice(AdviceCalculationCriteria criteria, FCIPosition fciPosition) {
        Multimap<SpecieType, OperationAdviceVO> specieTypeAdvices = ArrayListMultimap.create();

        RegulationLagOutcomeVO regulationLagOutcomeVO = fciCalculationService.calculatePositionDisarrangement(fciPosition);
        Map<SpecieType, Double> percentagePosition = regulationLagOutcomeVO.getPercentagePosition();
        percentagePosition.forEach((specieType, speciePercentage) -> {
            if (SpecieType.BOND == specieType) {
                OrderType orderType = Objects.requireNonNull(OrderType.valueOfSign(speciePercentage));
                bymaService.getBondsOrderByPrice(orderType).stream().limit(5).forEach(e ->
                        setSpecieTypeAdvice(calculatePercentageOverPriceToCoverValued(fciPosition, speciePercentage),
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
    private Double calculatePercentageOverPriceToCoverValued(FCIPosition fciPosition, Double speciePercentage) {
        Double priceToCover = calculatePriceToCover(fciPosition, speciePercentage);
        Double percentageOverPriceToCover = CalculationServiceHelper.calculatePercentageToCoverUniformly(5);
        return CalculationServiceHelper.calculatePercentageOverTotalValued(percentageOverPriceToCover, priceToCover);
    }

    private Double calculatePriceToCover(FCIPosition fciPosition, Double speciePercentage) {
        Double totalValuedPosition = fciPositionService.calculateTotalValuedPosition(fciPosition.getFciPositionList());
        return CalculationServiceHelper.calculatePercentageOverTotalValued(speciePercentage, totalValuedPosition);
    }

    @Override
    public Map<SpecieType, List<OperationAdviceVO>> advicePositionSellingByHighestPriceOverAverageBased(FCIPosition fciPosition) {
        return null;
    }
}
