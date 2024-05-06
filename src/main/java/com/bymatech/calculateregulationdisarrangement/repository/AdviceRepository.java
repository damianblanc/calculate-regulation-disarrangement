package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.Advice;
import com.bymatech.calculateregulationdisarrangement.domain.Report;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceRepository extends JpaRepository<Advice, Integer> {

  @Query("SELECT r FROM Advice r WHERE r.createdOn < :instant")
  List<Report> findAdvicesOlderThanIndicatedInstant(@Param("instant") Timestamp instant);

  @Query("SELECT r FROM Advice r WHERE r.createdOn >= :instantFrom AND r.createdOn <= :instantTo")
  List<Advice> findAdvicesByInstantRange(@Param("instantFrom") Timestamp instantFrom, @Param("instantTo") Timestamp instantTo);
}
