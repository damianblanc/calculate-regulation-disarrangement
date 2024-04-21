package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FCICompositionVO {
    private Integer id;
    private Integer specieTypeId;
    private String specieTypeName;
    private String percentage;
}
