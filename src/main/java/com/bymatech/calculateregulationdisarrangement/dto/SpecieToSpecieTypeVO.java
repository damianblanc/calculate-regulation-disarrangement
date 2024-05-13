package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SpecieToSpecieTypeVO {

    private Integer id;
    private String specieSymbol;
    private Integer fciSpecieTypeId;
    private String specieTypeName;
    private Integer fciReferencedPositionQuantity;
}
