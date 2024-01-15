package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.ReportType;
import com.bymatech.calculateregulationdisarrangement.dto.PositionPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import com.bymatech.calculateregulationdisarrangement.service.FCISummarizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/summarize")
public class FCISummarizeController {

    @Autowired
    private FCISummarizeService fciSummarizeService;

    @GetMapping()
    public SummarizeOverviewVO getSummarizedOverview() throws Exception {
        return fciSummarizeService.retrieveSummarizeOverview();
    }

    @GetMapping("/positions-per-month")
    public List<PositionPerMonthVO> listPositionsPerMonth() throws Exception {
        return fciSummarizeService.retrievePositionsPerMonth();
    }

}
