package com.bymatech.calculateregulationdisarrangement.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatHelper {

    public static String format(Double totalPosition) {
        Locale locale = new Locale("es-AR");
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
//        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) currency).getDecimalFormatSymbols();
//        String currencySymbol = decimalFormatSymbols.getCurrencySymbol();
//        decimalFormatSymbols.setCurrencySymbol("");
//        ((DecimalFormat) currency).setDecimalFormatSymbols(decimalFormatSymbols);

        return currency.format(totalPosition);
    }
}
