package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.Advice;
import com.bymatech.calculateregulationdisarrangement.repository.AdviceRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIAdviceService;
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
public class FCIAdviceServiceImpl implements FCIAdviceService {

  @Autowired
  private AdviceRepository adviceRepository;

  public Advice createAdvice() {
    Advice Advice = new Advice();
    Advice.setCreatedOn();
    return adviceRepository.save(Advice);
  }

  @Override
  public void deleteAdvice(Advice Advice) {
    adviceRepository.delete(Advice);
  }

  @Override
  public void deleteAdvices(List<Advice> Advices) {
    Advices.forEach(this::deleteAdvice);
  }

  @Override
//  @Scheduled(cron = "${task.execution.cron}")
  public void deleteObsoleteAdvices() {
    Instant oneYearAgo = Instant.now().minus(1, ChronoUnit.YEARS);
    adviceRepository.findAdvicesOlderThanIndicatedInstant(Timestamp.from(oneYearAgo));
  }

  @Override
  public List<Advice> listAdvicesByDateRangeNowAndOneYearAgo() {
    LocalDateTime oneYearAgo = LocalDateTime.now().minus(1, ChronoUnit.YEARS);
    Instant instantOneYearAgo = oneYearAgo.atZone(ZoneId.systemDefault()).toInstant();
    return adviceRepository.findAdvicesByInstantRange(Timestamp.from(instantOneYearAgo), Timestamp.from(Instant.now()));
  }

  @Override
  public Map<String, Integer> listAdvicesGroupedByMonthForOneYear() {
    Map<String, Integer> sortedMonthMap = new LinkedHashMap<>();

    Map<String, Integer> partialReportsGroupedList = listAdvicesMonthlyGrouped();
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
  public Map<String, Integer> listAdvicesMonthlyGrouped() {
    List<Advice> reports = listAdvicesByDateRangeNowAndOneYearAgo();
    Map<String, IntSummaryStatistics> groupedReportsPerMonth = reports.stream().map(Advice::getCreatedOn)
        .collect(Collectors.groupingBy(date -> DateOperationHelper.month(date.getMonth()), Collectors.summarizingInt(x -> 1)));

    return groupedReportsPerMonth.entrySet().stream().map(entry ->
            Map.entry(entry.getKey(), (int) entry.getValue().getCount()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Long getAdviceQuantity() {
    return adviceRepository.findAll().stream().count();
  }
}
