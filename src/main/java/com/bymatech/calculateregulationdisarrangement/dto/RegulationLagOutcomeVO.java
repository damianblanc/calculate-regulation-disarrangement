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

    /** Calculated Percentage Position biases for each Position {@link FCISpecieType} */
    private Map<FCISpecieType, Double> positionOverRegulationBiasPercentage;

    /** Calculated Percentage Position biases for each Position {@link FCISpecieType} */
    private Map<FCISpecieType, Double> positionOverRegulationBiasValued;

    /** Calculates Percentage of {@link FCISpecieType} related to total valued Position */
    private Map<FCISpecieType, Double> positionOverTotalPositionPercentage;

    /**   */
    private Map<FCISpecieType, Double> positionOverTotalPositionValued;

    /**   */
    private Map<FCISpecieType, Double> regulationPercentage;

    /**   */
    private Map<FCISpecieType, Double> regulationValued;

    /**   */
    private Map<FCISpecieType, Double> specieTypePercentageWeightRelativeToPosition;

    /**   */
    private Map<FCISpecieType, Double> specieTypeValueWeightRelativeToPosition;

    /**   */
    private Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToFCISpecieType;

    /**   */
    private Map<FCISpeciePosition, Double> specieValueWeightRelativeToFCISpecieType;

    /**   */
    private Map<FCISpeciePosition, Double> speciePercentageWeightRelativeToPosition;

    /**   */
    private Map<FCISpeciePosition, Double> specieValueWeightRelativeToPosition;
}
