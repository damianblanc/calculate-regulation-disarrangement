package com.bymatech.calculateregulationdisarrangement.util;

/**
 * Defines some constants to use over algorithms
 */
public class Constants {

    public static final Double TOTAL_PERCENTAGE = 100.00;

    public static final Integer PAGE_SIZE = 15;

    public static final Double CASH_NOT_DEFINED_MARKET_PRICE = -1.0;

    public static final Double MARKET_UNAVAILABLE_PRICES = 0.0;

    public static long begin(int pageNumber) {
        return (long) pageNumber * Constants.PAGE_SIZE;
    }

    public static long begin(int pageNumber, int pageSize) {
        return (long) pageNumber * pageSize;
    }

    public static long end(int pageNumber) {
        return (long) (pageNumber + 1) * Constants.PAGE_SIZE;
    }
}
