package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FCIReportService {

    ReportType createReportType(ReportType reportType);

    List<ReportType> listReportTypes();
}
