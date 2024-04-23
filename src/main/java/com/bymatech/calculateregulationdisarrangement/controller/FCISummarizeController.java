package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.dto.SummaryPerMonthVO;
import com.bymatech.calculateregulationdisarrangement.dto.SummarizeOverviewVO;
import com.bymatech.calculateregulationdisarrangement.service.FCISummarizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/summarize")
public class FCISummarizeController {

    @Autowired
    private FCISummarizeService fciSummarizeService;

    @GetMapping()
    public SummarizeOverviewVO getSummarizedOverview() throws Exception {
        return fciSummarizeService.retrieveSummarizeOverview();
    }

    @GetMapping("/regulations-per-month")
    public List<SummaryPerMonthVO> listRegulationsPerMonth() throws Exception {
        return fciSummarizeService.retrieveRegulationsPerMonth();
    }

    @GetMapping("/positions-per-month")
    public List<SummaryPerMonthVO> listPositionsPerMonth() throws Exception {
        return fciSummarizeService.retrievePositionsPerMonth();
    }

    @GetMapping("/positions-per-month/fci/{fciRegulationSymbol}")
    public List<SummaryPerMonthVO> listRegulationPositionsPerMonth(@PathVariable String fciRegulationSymbol) throws Exception {
        return fciSummarizeService.retrieveRegulationPositionsPerMonth(fciRegulationSymbol);
    }

    @GetMapping("/reports-per-month")
    public List<SummaryPerMonthVO> listReportsPerMonth() throws Exception {
        return fciSummarizeService.retrieveReportsPerMonth();
    }

    @GetMapping("/advices-per-month")
    public List<SummaryPerMonthVO> listAdvicesPerMonth() throws Exception {
        return fciSummarizeService.retrieveAdvicesPerMonth();
    }

}
