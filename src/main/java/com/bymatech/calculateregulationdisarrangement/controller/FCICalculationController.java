package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagVerboseVO;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class FCICalculationController {

    @Autowired
    private FCICalculationService fciCalculationService;

    @PostMapping(path="/calculate-disarrangement/fci/{symbol}/percentages", consumes="application/json", produces="application/json")
    public Map<SpecieType, Double> calculateDisarrangementPercentages(@PathVariable String symbol, @RequestBody FCISpeciePositionDTO fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementPercentages(symbol, fciPosition);
    }

    @PostMapping(path="/calculate-disarrangement/fci/{symbol}/valued", consumes="application/json", produces="application/json")
    public Map<SpecieType, Double> calculateDisarrangementValued(@PathVariable String symbol, @RequestBody FCISpeciePositionDTO fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementValued(symbol, fciPosition);
    }

    @PostMapping(path="/calculate-disarrangement/fci/{symbol}", consumes="application/json", produces="application/json")
    public RegulationLagOutcomeVO calculateDisarrangement(@PathVariable String symbol, @RequestBody FCISpeciePositionDTO fciPosition) {
        return fciCalculationService.calculatePositionDisarrangement(symbol, fciPosition);
    }

    @PostMapping(path="/calculate-disarrangement/fci/{symbol}/verbose", consumes="application/json", produces="application/json")
    public RegulationLagVerboseVO calculateDisarrangementVerbose(@PathVariable String symbol, @RequestBody FCISpeciePositionDTO fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementVerbose(symbol, fciPosition);
    }

}
