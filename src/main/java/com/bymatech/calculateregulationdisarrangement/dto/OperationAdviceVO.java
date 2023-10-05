package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.OperationAdvice;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsExclude;

@Data
@AllArgsConstructor
public class OperationAdviceVO {

    private String specieName;

    private OperationAdvice operationAdvice;

    private Integer quantity;

    private Double price;

    public boolean canAdvice() {
        return quantity > 0;
    }
}
