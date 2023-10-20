package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}", consumes="application/json", produces="application/json")
    public RegulationLagOutcomeVO calculateDisarrangement(@PathVariable String symbol, @PathVariable String id) throws JsonProcessingException {
        return fciCalculationService.calculatePositionBiasById(symbol, id);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-percentages", consumes="application/json", produces="application/json")
    public FCIRegulationPercentageVO calculateRegulationPercentages(@PathVariable String symbol, @PathVariable String id) throws JsonProcessingException {
        return fciCalculationService.calculateRegulationPercentages(symbol, id);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-valued", consumes="application/json", produces="application/json")
    public FCIRegulationValuedVO calculateRegulationValued(@PathVariable String symbol, @PathVariable String id) throws JsonProcessingException {
        return fciCalculationService.calculateRegulationValued(symbol, id);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages", consumes="application/json", produces="application/json")
    public FCIPositionPercentageVO calculateBiasPercentages(@PathVariable String symbol, @PathVariable String id) throws JsonProcessingException {
        return fciCalculationService.calculatePositionBiasPercentages(symbol, id);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/valued", consumes="application/json", produces="application/json")
    public FCIPositionValuedVO calculateBiasValued(@PathVariable String symbol, @PathVariable String id) throws JsonProcessingException {
        return fciCalculationService.calculatePositionBiasValued(symbol, id);
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
