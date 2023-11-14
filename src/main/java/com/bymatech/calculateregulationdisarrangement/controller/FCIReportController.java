package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import com.bymatech.calculateregulationdisarrangement.service.FCIReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Comprehends all reporting operations
 */
@RestController
@RequestMapping("/api/v1/component/")
public class FCIReportController {

    @Autowired
    private FCIReportService fciReportService;


    @PostMapping("/report")
    public ReportType createReportType(@RequestBody ReportType reportType) {
        return fciReportService.createReportType(reportType);
    }

    @GetMapping("report")
    public List<ReportType> listReportTypes() {
        return fciReportService.listReportTypes();
    }
}
