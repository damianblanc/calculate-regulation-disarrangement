package com.bymatech.calculateregulationdisarrangement.util;

/**
 * Groups all exception messages for managing purposes
 */
public enum ExceptionMessage {

    TOTAL_PERCENTAGE(String.format("Composition Percentage does not close to %d", Constants.TOTAL_PERCENTAGE.intValue())),
    REGULATION_SPECIE_TYPE_DOES_NOT_MATCH("FCI Composition Specie Types does not match - [Missing specie type required %s]"),
    ADVISE_CRITERIA_NOT_DEFINED("Indicated advise Criteria is not defined - [%s]"),
    FCI_REGULATION_ENTITY_NOT_FOUND("FCI Regulation cannot be found with symbol [%s]"),
    ;

    public final String msg;

    private ExceptionMessage(String message) {
        this.msg = message;
    }
}

