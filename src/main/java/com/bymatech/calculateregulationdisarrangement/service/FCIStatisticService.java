package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.StatisticDTO;
import org.springframework.stereotype.Service;

@Service
public interface FCIStatisticService {

  void updateStatistics(StatisticDTO statisticDTO);

  StatisticDTO retrieveStatistics();

}
