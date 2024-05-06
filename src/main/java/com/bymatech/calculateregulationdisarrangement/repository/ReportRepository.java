package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieTypeGroup;
import com.bymatech.calculateregulationdisarrangement.domain.Report;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

  @Query("SELECT r FROM Report r WHERE r.createdOn < :instant")
  List<Report> findReportsOlderThanIndicatedInstant(@Param("instant") Timestamp instant);

  @Query("SELECT r FROM Report r WHERE r.createdOn BETWEEN :instantFrom AND :instantTo")
  List<Report> findReportsByInstantRange(@Param("instantFrom") Timestamp instantFrom, @Param("instantTo") Timestamp instantTo);
}
