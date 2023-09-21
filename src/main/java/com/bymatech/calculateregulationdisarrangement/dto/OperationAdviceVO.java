package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.OperationAdvice;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationAdviceVO {

    private String specieName;

    private OperationAdvice operationAdvice;

    private Integer quantity;
}
