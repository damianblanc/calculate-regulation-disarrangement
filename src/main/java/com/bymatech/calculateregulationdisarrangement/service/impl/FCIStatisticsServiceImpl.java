package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.Statistic;
import com.bymatech.calculateregulationdisarrangement.dto.StatisticDTO;
import com.bymatech.calculateregulationdisarrangement.repository.FCIStatisticsRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIStatisticService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCIStatisticsServiceImpl implements FCIStatisticService {

    @Autowired
    private FCIStatisticsRepository fciStatisticsRepository;


  @Override
  public void updateStatistics(StatisticDTO statisticDTO) {
      fciStatisticsRepository.save(Statistic.builder()
          .reportQuantity(statisticDTO.getReportQuantity())
          .adviceQuantity(statisticDTO.getAdviceQuantity()).build());
  }

  @Override
  public StatisticDTO retrieveStatistics() {
    List<Statistic> statistics = fciStatisticsRepository.findAll(); // Only one row
    if (statistics.isEmpty()) {
      return new StatisticDTO(0, 0);
    }
    return new StatisticDTO(statistics.get(0).getReportQuantity(), statistics.get(0).getAdviceQuantity());
  }
}
