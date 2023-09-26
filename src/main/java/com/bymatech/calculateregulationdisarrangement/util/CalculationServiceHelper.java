package com.bymatech.calculateregulationdisarrangement.util;

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
        return (int) Math.round(totalAmountToCover / speciePrice);
    }
}
