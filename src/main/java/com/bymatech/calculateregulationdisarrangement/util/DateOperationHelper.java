package com.bymatech.calculateregulationdisarrangement.util;

import com.google.common.base.Strings;

import java.sql.Date;

/**
 * Contains various utilities to work with dates in order to support other operations
 */
public class DateOperationHelper {

    public static Boolean isInRange(String c, String from, String to) {
        if (Strings.isNullOrEmpty(from) && Strings.isNullOrEmpty(to)) return true;
        if (Strings.isNullOrEmpty(from) && !Strings.isNullOrEmpty(to)) {
            return Date.valueOf(c).before(Date.valueOf(to));
        }
        if (!Strings.isNullOrEmpty(from) && Strings.isNullOrEmpty(to)) {
            return Date.valueOf(from).before(Date.valueOf(c));
        }
        if (!Strings.isNullOrEmpty(from) && !Strings.isNullOrEmpty(to)) {
            return Date.valueOf(from).before(Date.valueOf(c)) && Date.valueOf(c).before(Date.valueOf(to));
        }
        return true;
    }
}
