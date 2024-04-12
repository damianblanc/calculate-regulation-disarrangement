package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.domain.OperationAdvice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OperationAdviceSpecieTypeFlatFormat {

  private String specieType;
  private String specieTypeGroup;
  private String specieName;
  private OperationAdvice operationAdvice;
  private Double quantity;
  private Double price;
  private Double value;
}