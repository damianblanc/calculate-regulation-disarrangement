package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.Report;
import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import com.bymatech.calculateregulationdisarrangement.repository.ReportRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FCIReportService {

    /**
     * Creates a Report metadata, currently only registering when a report is performed
     */
    Report createReport();

    void deleteReport(Report report);

    /**
     * Given a list of reports deletes them all
     */
    void deleteReports(List<Report> reports);

    /**
     * Deletes Reports that are one year older from current date
     */
    void deleteObsoleteReports();

    List<Report> listReportsByDateRangeNowAndOneYearAgo();

    /**
     * Lists sorted on year from current month backwards grouped record list
     */
    Map<String, Integer> listReportsGroupedByMonthForOneYear();

    /**
     * Lists monthly grouped available reports
     */
    Map<String, Integer> listReportsMonthlyGrouped();

    /**
     * Count all available reports to inform quantity
     */
    Long getReportQuantity();
}
