package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.StatisticComponent;
import com.bymatech.calculateregulationdisarrangement.dto.StatisticDTO;
import com.bymatech.calculateregulationdisarrangement.service.FCIStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/statistic")
public class StatisticController {

  @Autowired
  private FCIStatisticService fciStatisticService;

  @GetMapping()
  private StatisticDTO retrieveStatistics() {
    return fciStatisticService.retrieveStatistics();
  }

  @PutMapping("update/report-quantity")
  public void updateStatisticsReportQuantity(@RequestBody StatisticDTO statisticDTO) {
      fciStatisticService.updateStatistics(statisticDTO, StatisticComponent.REPORT);
  }

  @PutMapping("update/advice-quantity")
  public void updateStatisticsAdvicesQuantity(@RequestBody StatisticDTO statisticDTO) {
    fciStatisticService.updateStatistics(statisticDTO, StatisticComponent.ADVICE);
  }

}
