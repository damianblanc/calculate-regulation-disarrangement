package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegulationLagVerboseVO {

    RegulationLagOutcomeVO regulationLagOutcome;

    FCISpeciePositionDTO fciPosition;
}
