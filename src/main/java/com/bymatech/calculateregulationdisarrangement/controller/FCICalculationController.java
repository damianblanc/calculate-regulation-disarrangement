package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.RegulationLagOutcomeVO;
import com.bymatech.calculateregulationdisarrangement.service.FCICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FCICalculationController {

    @Autowired
    private FCICalculationService fciCalculationService;

    @PostMapping("/calculate-disarrangement/percentages")
    public RegulationLagOutcomeVO calculateDisarrangementPercentages(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangement(fciPosition);
    }

    @PostMapping("/calculate-disarrangement/valued")
    public RegulationLagOutcomeVO calculateDisarrangementValued(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangement(fciPosition);
    }

    @PostMapping("/calculate-disarrangement")
    public RegulationLagOutcomeVO calculateDisarrangement(@RequestBody FCIPosition fciPosition) {
        return fciCalculationService.calculatePositionDisarrangement(fciPosition);
    }

}
