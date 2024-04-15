package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCIStatisticsRepository extends JpaRepository<Statistic, Integer> {

}
