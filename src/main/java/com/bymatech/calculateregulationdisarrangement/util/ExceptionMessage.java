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

    FCI_COMPOSITION_ENTITY_NOT_FOUND("FCI Composition cannot be found with id [%s]"),

    FCI_POSITION_ENTITY_NOT_FOUND("FCI Regulation Position [%s] cannot be found with id [%s]"),

    SPECIE_TYPE_ENTITY_NOT_FOUND("Specie Type cannot be found with name [%s]"),

    SPECIE_TYPE_ASSOCIATION_ENTITY_NOT_FOUND("Specie Type association cannot be found for specie symbol [%s]"),

    SPECIE_TYPE_GROUP_ENTITY_NOT_FOUND("Specie Type Group cannot be found with name [%s]"),

    SPECIE_TYPE_NAME_ALREADY_EXISTS("Specie Type name already exists, please provide a new name"),

    POSITION_CONTAINS_NOT_RETRIEVED_SPECIE("Position contains specie [%s] which is not informed by Market"),

    MARKET_EQUITY_INFORMATION_NOT_AVAILABLE("Market Equities Information is not currently available, try again later"),

    MARKET_BOND_INFORMATION_NOT_AVAILABLE("Market Bonds Information is not currently available, try again later"),

    MARKET_CEDEAR_INFORMATION_NOT_AVAILABLE("Market Cedears Information is not currently available, try again later"),

    MARKET_PRICE_NOT_AVAILABLE("Market prices are not available at this moment, try again later"),

    MARKET_TIMEOUT_RESPONSE("Market is not currently available, try again later"),

    SPECIE_TYPE_DOES_NOT_EXIST("Specie Type [%s] has not been defined"),

    SPECIE_TYPE_DOES_NOT_BELONG_TO_REGULATION("Specie Type [%s] is not defined in FCI Regulation [%s]"),

    INVALID_POSITION_FIELD_NAME("Uploaded Position does not contains an expected column name [%s]"),

    INVALID_POSITION_SPECIE_SYMBOL("Uploaded Position contains an unrecognized specie identified with symbol [%s]"),

    INVALID_POSITION_SPECIE_GROUP("Uploaded Position contains an unrecognized FCI Specie Type Group identified with name [%s]"),

    INVALID_POSITION_REGULATION_COMPOSITION("Uploaded Position does not include any Specie for Specie Type [%s] defined in FCI Regulation [%s]"),

    CASH_SPECIE_TYPE_NOT_INCLUDED_POSITION("Cash Specie Type must be included in uploaded Position"),

    CASH_SPECIE_TYPE_PRICE_NOT_DEFINED("Cash Specie Type must define Price"),

    CASH_SPECIE_TYPE_QUANTITY_NOT_ONE("Cash Specie Type must define Quantity equals to one"),

    SPECIE_TO_SPECIE_TYPE_DOES_NOT_EXIST("Specie symbol [%s] is not associated to any Specie Type"),

    SPECIE_TYPE_DOES_NOT_BELONG_TO_GROUP("Specie Type [%s - %s] does not belong to indicated group [%s]"),

    SPECIE_TYPE_CANNOT_BE_DELETED_SPECIES_BOUND("Specie Type [%s - %s] cannot be deleted since it has associated species"),

    SPECIE_TYPE_CANNOT_BE_DELETED_POSITION_BOUND("Specie Type [%s - %s] cannot be deleted since it has positions referring them"),

    SPECIE_NOT_BOUND_TO_ANY_SPECIE_TYPE("Specie [%s] is not associated to any specie type"),

    SPECIE_REFERENCED_BY_POSITION("Cannot change Specie Type to Specie [%s], due to it is referenced by [%d] FCI Positions. These are some of them: [%s]"),

    USER_NAME_DOES_NOT_EXIST("User name is incorrect or not registered"),

    USER_INCORRECT_PASSWORD("User password is incorrect or not recognized"),

    USER_NAME_ALREADY_REGISTERED("User name is incorrect or not registered"),

    ;

    public final String msg;

    private ExceptionMessage(String message) {
        this.msg = message;
    }
}

