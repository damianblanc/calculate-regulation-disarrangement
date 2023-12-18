package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OperationAdviceSpecieType implements Comparable<OperationAdviceSpecieType> {

    private Integer id;
    private String specieType;
    private String specieTypeGroup;
    private Collection<OperationAdviceVO> operationAdvices;

    @Override
    public int compareTo(@NotNull OperationAdviceSpecieType operationAdviceSpecieType) {
        if (specieTypeGroup.equals(operationAdviceSpecieType.getSpecieTypeGroup())) return 0;
        return specieTypeGroup.length() < operationAdviceSpecieType.getSpecieTypeGroup().length() ? -1 : 1;
    }
}
