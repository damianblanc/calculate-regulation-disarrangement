package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class FCICalculationController {

    @Autowired
    private FCICalculationService fciCalculationService;

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/refresh/{refresh}", consumes="application/json", produces="application/json")
    public RegulationLagOutcomeVO calculateBias(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-percentages", consumes="application/json", produces="application/json")
    public FCIRegulationPercentageVO calculateRegulationPercentages(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciCalculationService.calculateRegulationPercentages(symbol, id);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-valued", consumes="application/json", produces="application/json")
    public FCIRegulationValuedVO calculateRegulationValued(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciCalculationService.calculateRegulationValued(symbol, id);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/refresh/{refresh}", consumes="application/json", produces="application/json")
    public FCIPositionPercentageVO calculateBiasPercentages(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBiasPercentages(symbol, id, refresh);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/valued/refresh/{refresh}", consumes="application/json", produces="application/json")
    public FCIPositionValuedVO calculateBiasValued(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBiasValued(symbol, id, refresh);
    }

}
