package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface FCIReportTypeService {

  ReportType createReportType(ReportType reportType);

  List<ReportType> listReportTypes();

}
