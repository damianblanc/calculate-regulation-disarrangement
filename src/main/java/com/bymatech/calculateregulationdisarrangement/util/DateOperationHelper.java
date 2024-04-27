package com.bymatech.calculateregulationdisarrangement.util;

import com.google.common.base.Strings;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contains various utilities to work with dates in order to support other operations
 */
public class DateOperationHelper {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static List<String> months = List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    public static boolean isInRange(String fromDate, String toDate, String elementTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = LocalDate.parse(fromDate, formatter);
        LocalDate to = LocalDate.parse(toDate, formatter);
        LocalDate elementDate = LocalDate.parse(elementTimestamp, DateTimeFormatter.ISO_DATE);

        return elementDate.isAfter(from.minusDays(1)) && elementDate.isBefore(to.plusDays(1));
    }

    public static String month(int index) { return months.get(index); }

    public static String getMonth(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return localDateTime.format(formatter);
    }
}
