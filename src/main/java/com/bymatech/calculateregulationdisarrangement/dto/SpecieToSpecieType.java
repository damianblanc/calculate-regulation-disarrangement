package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SpecieToSpecieType {

    private String specieSymbol;
    private String specieTypeName;
    private String specieTypeGroupName;
}
