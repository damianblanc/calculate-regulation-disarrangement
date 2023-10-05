package com.bymatech.calculateregulationdisarrangement.util;

import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;

import java.util.List;
import java.util.Map;

/**
 * Helper class to concentrate calculations
 */
public class CalculationServiceHelper {


    public static Double calculatePercentageOverTotalValued(Double percentage, Double total) {
        return percentage * total / Constants.TOTAL_PERCENTAGE;
    }

    public static Double calculatePercentageToCoverUniformly(Integer quantity) {
        return (double) (Constants.TOTAL_PERCENTAGE / quantity);
    }

    public static Integer calculateSpecieQuantityToCover(Double totalAmountToCover, Double speciePrice) {
        return (int) Math.abs(Math.round(totalAmountToCover / speciePrice));
    }

    public static Integer calculateMinNumber(Integer a, Integer b) {
        return a > b ? b : a;
    }

    public static List<SpeciePosition> getFciPositionListFilteredBySpecieType(
            List<SpeciePosition> fciPositionList, SpecieType specieType) {
        return fciPositionList.stream().filter(e -> e.getSpecieType() == specieType).toList();
    }

    public static Double summarizePositionList(Map<SpecieType, Double> summarizedPosition) {
        return summarizedPosition.values().stream().reduce(Double::sum).orElseThrow();
    }
}
