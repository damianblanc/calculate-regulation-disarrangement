package com.bymatech.calculateregulationdisarrangement.util;

import static com.bymatech.calculateregulationdisarrangement.util.Constants.TOTAL_PERCENTAGE;

/**
 * Groups all exception messages for managing purposes
 */
public enum ExceptionMessage {

    INTERNAL_PERCENTAGE_SUM_REDUCTION("Composition percentage sum failed"),
    TOTAL_PERCENTAGE(String.format("Composition Percentage does not close to %d", Constants.TOTAL_PERCENTAGE.intValue()));


    public final String msg;

    private ExceptionMessage(String message) {
        this.msg = message;
    }
}

