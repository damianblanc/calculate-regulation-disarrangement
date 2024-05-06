package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.Report;
import com.bymatech.calculateregulationdisarrangement.repository.ReportRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import com.bymatech.calculateregulationdisarrangement.util.DateOperationHelper;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCIReportServiceImpl implements FCIReportService {

  @Autowired
  private ReportRepository reportRepository;

  public Report createReport() {
    Report report = new Report();
    report.setCreatedOn();
    return reportRepository.save(report);
  }

  @Override
  public void deleteReport(Report report) {
    reportRepository.delete(report);
  }

  @Override
  public void deleteReports(List<Report> reports) {
    reports.forEach(this::deleteReport);
  }

  @Override
//  @Scheduled(cron = "${task.execution.cron}")
  public void deleteObsoleteReports() {
    Instant oneYearAgo = Instant.now().minus(1, ChronoUnit.YEARS);
    reportRepository.findReportsOlderThanIndicatedInstant(Timestamp.from(oneYearAgo));
  }

  @Override
  public List<Report> listReportsByDateRangeNowAndOneYearAgo() {
    LocalDateTime oneYearAgo = LocalDateTime.now().minus(1, ChronoUnit.YEARS);
    Instant instantOneYearAgo = oneYearAgo.atZone(ZoneId.systemDefault()).toInstant();
    return reportRepository.findReportsByInstantRange(Timestamp.from(instantOneYearAgo), Timestamp.from(Instant.now()));
  }

  @Override
  public Map<String, Integer> listReportsGroupedByMonthForOneYear() {
    Map<String, Integer> sortedMonthMap = new LinkedHashMap<>();

    Map<String, Integer> partialReportsGroupedList = listReportsMonthlyGrouped();
    Map<String, Integer> groupedReportsPerMonth = DateOperationHelper.months.stream().map(month ->
            Map.entry(month, partialReportsGroupedList.getOrDefault(month, 0)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Month currentMonth = LocalDate.now().getMonth();
    for (int i = 0; i < 12; i++) {
      String m = currentMonth.minus(i).toString();
      String k = m.substring(0, 1).toUpperCase() + m.substring(1).toLowerCase();
      sortedMonthMap.put(k, groupedReportsPerMonth.get(k));
    }

    return sortedMonthMap;
  }

  @Override
  public Map<String, Integer> listReportsMonthlyGrouped() {
    List<Report> reports = listReportsByDateRangeNowAndOneYearAgo();
    Map<String, IntSummaryStatistics> groupedReportsPerMonth = reports.stream().map(Report::getCreatedOn)
        .collect(Collectors.groupingBy(date -> DateOperationHelper.month(date.getMonth()), Collectors.summarizingInt(x -> 1)));

    return groupedReportsPerMonth.entrySet().stream().map(entry ->
            Map.entry(entry.getKey(), (int) entry.getValue().getCount()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Long getReportQuantity() {
    return reportRepository.findAll().stream().count();
  }
}
