package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.Advice;
import java.util.List;
import java.util.Map;

public interface FCIAdviceService {
  /**
   * Creates a Advice metadata, currently only registering when a Advice is performed
   */
  Advice createAdvice();

  void deleteAdvice(Advice Advice);

  /**
   * Given a list of Advices deletes them all
   */
  void deleteAdvices(List<Advice> Advices);

  /**
   * Deletes Advices that are one year older from current date
   */
  void deleteObsoleteAdvices();

  List<Advice> listAdvicesByDateRangeNowAndOneYearAgo();

  /**
   * Lists sorted on year from current month backwards grouped record list
   */
  Map<String, Integer> listAdvicesGroupedByMonthForOneYear();

  /**
   * Lists monthly grouped available reports
   */
  Map<String, Integer> listAdvicesMonthlyGrouped();

  /**
   * Count all available advices to inform quantity
   */
  Long getAdviceQuantity();
}
