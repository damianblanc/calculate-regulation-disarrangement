package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.Statistic;
import com.bymatech.calculateregulationdisarrangement.domain.StatisticComponent;
import com.bymatech.calculateregulationdisarrangement.dto.StatisticDTO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIStatisticsRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIAdviceService;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import com.bymatech.calculateregulationdisarrangement.service.FCIStatisticService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCIStatisticsServiceImpl implements FCIStatisticService {

    @Autowired
    private FCIStatisticsRepository fciStatisticsRepository;

    @Autowired
    private FCIReportService fciReportService;

    @Autowired
    private FCIAdviceService fciAdviceService;


  @Override
  public void updateStatistics(StatisticDTO statisticDTO, StatisticComponent statisticComponent) {
    List<Statistic> statistics = fciStatisticsRepository.findAll();
    if(!statistics.isEmpty()) {
      Statistic statistic = statistics.get(0);
      statistic.setAdviceQuantity(statisticDTO.getAdviceQuantity());
      statistic.setReportQuantity(statisticDTO.getReportQuantity());
      fciStatisticsRepository.save(statistic);
    }
    if (StatisticComponent.REPORT == statisticComponent) fciReportService.createReport();
    if (StatisticComponent.ADVICE == statisticComponent) fciAdviceService.createAdvice();
  }

  @Override
  public StatisticDTO retrieveStatistics() {
    List<Statistic> statistics = fciStatisticsRepository.findAll(); // Only one row
    if (statistics.isEmpty()) {
      fciStatisticsRepository.save(Statistic.builder().adviceQuantity(0).reportQuantity(0).build());
      return new StatisticDTO(0, 0);

    }
    return new StatisticDTO(statistics.get(0).getReportQuantity(), statistics.get(0).getAdviceQuantity());
  }
}
