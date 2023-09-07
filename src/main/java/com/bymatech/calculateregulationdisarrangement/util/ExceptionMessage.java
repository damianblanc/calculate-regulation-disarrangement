package com.bymatech.calculateregulationdisarrangement.util;

import static com.bymatech.calculateregulationdisarrangement.util.Constants.TOTAL_PERCENTAGE;

/**
 * Groups all exception messages for managing purposes
 */
public enum ExceptionMessage {

    TOTAL_PERCENTAGE(String.format("Composition Percentage does not close to %d", Constants.TOTAL_PERCENTAGE.intValue())),
    REGULATION_SPECIE_TYPE_DOES_NOT_MATCH("FCI Composition Specie Types does not match - [Missing specie type required %s]");

    public final String msg;

    private ExceptionMessage(String message) {
        this.msg = message;
    }
}

