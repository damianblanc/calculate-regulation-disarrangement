package com.bymatech.calculateregulationdisarrangement.util;

import com.google.common.base.Strings;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contains various utilities to work with dates in order to support other operations
 */
public class DateOperationHelper {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<String> months = List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

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

    public static String month(int index) { return months.get(index); }

    public static String getMonth(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return localDateTime.format(formatter);
    }
}
