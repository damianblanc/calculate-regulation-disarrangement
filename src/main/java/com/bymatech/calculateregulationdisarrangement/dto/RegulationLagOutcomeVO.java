package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Comprehends disarrangement processing outcome in order to be exposed in API response
 */
@Getter
@AllArgsConstructor
public class RegulationLagOutcomeVO {

    private Map<SpecieType, Double> regulationLags;
    private Map<SpecieType, Double> regulationValuedLags;
    private Map<SpecieType, Double> positionPercentageBias;
    private Map<SpecieType, Double> positionValuedBias;
    private Map<SpecieType, Double> regulationPercentage;
    private Map<SpecieType, Double> regulationValued;

}
