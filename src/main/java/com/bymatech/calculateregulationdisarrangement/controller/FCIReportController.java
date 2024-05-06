package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Comprehends all reporting operations
 */
@RestController
@RequestMapping("/api/v1/component")
public class FCIReportController {
    @Autowired
    private FCIReportService fciReportService;
    @Autowired
    private FCIReportTypeService fciReportTypeService;


    @PostMapping("/report")
    public ReportType createReportType(@RequestBody ReportType reportType) {
        return fciReportTypeService.createReportType(reportType);
    }

    @GetMapping("/report")
    public List<ReportType> listReportTypes() {
        return fciReportTypeService.listReportTypes();
    }

    @GetMapping("/report/delete-obsolete")
    public void deleteObsoleteReports() {
        fciReportService.deleteObsoleteReports();
    }
}
