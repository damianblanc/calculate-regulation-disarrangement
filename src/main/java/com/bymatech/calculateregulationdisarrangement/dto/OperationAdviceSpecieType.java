package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class OperationAdviceSpecieType {

    private Integer id;
    private String specieType;
    private Collection<OperationAdviceVO> operationAdvices;
}
