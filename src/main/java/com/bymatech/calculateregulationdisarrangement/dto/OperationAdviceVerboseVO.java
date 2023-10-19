package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class OperationAdviceVerboseVO {

    private Set<FCIComposition> fciRegulationComposition;

    private RegulationLagOutcomeVO regulationLagOutcomeVO;

    private List<OperationAdviceSpecieType> operationAdvicesVO;
}
