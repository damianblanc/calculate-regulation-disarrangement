package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Comprehends disarrangement processing outcome in order to be exposed in API response
 */
@Getter
@AllArgsConstructor
public class RegulationLagOutcomeVO {

    private Map<FCISpecieType, Double> regulationLags;
    private Map<FCISpecieType, Double> regulationValuedLags;
    private Map<FCISpecieType, Double> positionPercentageBias;
    private Map<FCISpecieType, Double> positionValuedBias;
    private Map<FCISpecieType, Double> regulationPercentage;
    private Map<FCISpecieType, Double> regulationValued;
    private Map<FCISpecieType, Double> specieTypePercentageWeightRelativeToPosition;
    private Map<FCISpecieType, Double> specieTypeValueWeightRelativeToPosition;
    private Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToFCISpecieType;
    private Map<FCISpeciePosition, Double> specieValueWeightRelativeToFCISpecieType;
    private Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToPosition;
    private Map<FCISpeciePosition, Double> specieValueWeightRelativeToPosition;
}
