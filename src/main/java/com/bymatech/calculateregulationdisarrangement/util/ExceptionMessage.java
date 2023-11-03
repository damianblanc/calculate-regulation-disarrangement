package com.bymatech.calculateregulationdisarrangement.util;

/**
 * Groups all exception messages for managing purposes
 */
public enum ExceptionMessage {

    TOTAL_PERCENTAGE(String.format("Composition Percentage does not close to %d", Constants.TOTAL_PERCENTAGE.intValue())),

    REGULATION_SPECIE_TYPE_DOES_NOT_MATCH("FCI Composition Specie Types does not match - [Missing specie type required %s]"),

    ADVISE_CRITERIA_NOT_DEFINED("Indicated advise Criteria is not defined - [%s]"),

    ADVISE_CRITERIA_PARAMETER_NOT_DEFINED("Indicated advise Criteria parameters are not configured - [%s]"),

    FCI_REGULATION_ENTITY_NOT_FOUND("FCI Regulation cannot be found with symbol [%s]"),

    FCI_POSITION_ENTITY_NOT_FOUND("FCI Regulation Position [%s] cannot be found with id [%s]"),

    SPECIE_TYPE_ENTITY_NOT_FOUND("Specie Type Group cannot be found with name [%s]"),

    SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND("Specie Type cannot be found with name [%s]"),
    ;

    public final String msg;

    private ExceptionMessage(String message) {
        this.msg = message;
    }
}

