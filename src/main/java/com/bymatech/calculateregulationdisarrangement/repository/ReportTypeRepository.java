package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {

}
