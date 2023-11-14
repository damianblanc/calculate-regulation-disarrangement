package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.*;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class FCICalculationController {

    @Autowired
    private FCICalculationService fciCalculationService;

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-percentages", produces="application/json")
    public FCIRegulationPercentageVO calculateRegulationPercentages(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciCalculationService.calculateRegulationPercentages(symbol, id);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/regulation-valued", produces="application/json")
    public FCIRegulationValuedVO calculateRegulationValued(@PathVariable String symbol, @PathVariable String id) throws Exception {
        return fciCalculationService.calculateRegulationValued(symbol, id);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/refresh/{refresh}", produces="application/json")
    public RegulationLagOutcomeVO calculateBias(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/refresh/{refresh}", produces="application/json")
    public FCIPositionPercentageVO calculateBiasPercentages(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBiasPercentages(symbol, id, refresh);
    }
    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/valued/refresh/{refresh}", produces="application/json")
    public FCIPositionValuedVO calculateBiasValued(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBiasValued(symbol, id, refresh);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentage-valued/refresh/{refresh}", produces="application/json")
    public List<FCIPercentageAndValuedVO> calculateBiasPercentageValued(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBiasPercentageValued(symbol, id, refresh);
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/species-relative-to-position/refresh/{refresh}", produces="application/json")
    public Map<FCISpeciePosition, Double> calculateBiasPercentagesSpeciesRelativeToPosition(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpeciePercentageWeightRelativeToPosition();
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/valued/specie-relative-to-position/refresh/{refresh}", produces="application/json")
    public Map<FCISpeciePosition, Double> calculateBiasValuedSpeciesRelativeToPosition(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpecieValueWeightRelativeToPosition();
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/specie-relative-to-specie-type/refresh/{refresh}", produces="application/json")
    public Map<FCISpeciePosition, Double> calculateBiasPercentagesSpecieRelativeToSpecieType(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpeciePercentageWeightRelativeToFCISpecieType();
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/valued/specie-relative-to-specie-type/refresh/{refresh}", produces="application/json")
    public Map<FCISpeciePosition, Double> calculateBiasValuedSpecieRelativeToSpecieType(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpecieValueWeightRelativeToFCISpecieType();
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/specie-type-relative-position/refresh/{refresh}", consumes="application/json", produces="application/json")
    public Map<FCISpecieType, Double> calculateBiasPercentagesSpecieTypeRelativeToPosition(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpecieTypePercentageWeightRelativeToPosition();
    }

    @GetMapping(path="/calculate-bias/fci/{symbol}/position/{id}/percentages/specie-type-relative-position/refresh/{refresh}", produces="application/json")
    public Map<FCISpecieType, Double> calculateBiasValuedSpecieTypeRelativeToPosition(@PathVariable String symbol, @PathVariable String id, @PathVariable Boolean refresh) throws Exception {
        return fciCalculationService.calculatePositionBias(symbol, id, refresh).getSpecieTypeValueWeightRelativeToPosition();
    }

}
