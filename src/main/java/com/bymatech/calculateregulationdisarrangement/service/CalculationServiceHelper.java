package com.bymatech.calculateregulationdisarrangement.service;

/**
 * Helper class to concentrate calculations
 */
public class CalculationServiceHelper {

    private static final Integer ONE_HUNDRED_PERCENT = 100;

    public static Double calculatePercentageOverTotalValued(Double percentage, Double total) {
        return percentage * total / 100;
    }

    public static Double calculatePercentageToCoverUniformly(Integer quantity) {
        return (double) (ONE_HUNDRED_PERCENT / quantity);
    }

    public static Integer calculateSpecieQuantityToCover(Double totalAmountToCover, Double speciePrice) {
        return (int) Math.round(totalAmountToCover / speciePrice);
    }
}
