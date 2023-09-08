package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagVerboseVO;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class FCICalculationController {

    @Autowired
    private FCICalculationService fciCalculationService;

    @PostMapping("/calculate-disarrangement/percentages")
    public Map<SpecieType, Double> calculateDisarrangementPercentages(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementPercentages(fciPosition);
    }

    @PostMapping("/calculate-disarrangement/valued")
    public Map<SpecieType, Double> calculateDisarrangementValued(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementValued(fciPosition);
    }

    @PostMapping("/calculate-disarrangement")
    public RegulationLagOutcomeVO calculateDisarrangement(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangement(fciPosition);
    }

    @PostMapping("/calculate-disarrangement/verbose")
    public RegulationLagVerboseVO calculateDisarrangementVerbose(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangementVerbose(fciPosition);
    }

}
