package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import com.bymatech.calculateregulationdisarrangement.repository.ReportTypeRepository;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Performs {@link ReportType} operations
 */
@Service
public class FCIReportTypeImpl implements FCIReportService {

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    @Override
    public ReportType createReportType(ReportType reportType) {
        return reportTypeRepository.save(reportType);
    }

    @Override
    public List<ReportType> listReportTypes() {
        return reportTypeRepository.findAll();
    }
}
