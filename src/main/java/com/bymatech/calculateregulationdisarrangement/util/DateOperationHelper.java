package com.bymatech.calculateregulationdisarrangement.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contains various utilities to work with dates in order to support other operations
 */
public class DateOperationHelper {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<String> months = List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    public static boolean isInRange(String fromDate, String toDate, String elementTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = LocalDate.parse(fromDate, formatter);
        LocalDate to = LocalDate.parse(toDate, formatter);
        LocalDate elementDateTime = LocalDateTime.parse(elementTimestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .toLocalDate();

        return elementDateTime.isAfter(from.minusDays(1)) && elementDateTime.isBefore(to.plusDays(1));
    }

    public static String month(int index) { return months.get(index); }

    public static String getMonth(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return localDateTime.format(formatter);
    }
}
