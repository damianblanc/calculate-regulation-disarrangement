package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FCIPositionCompositionVO {

    private Integer id;

    private String specieGroup;

    private String specieType;

//    private String specieName;

    private String specieSymbol;

    private Double marketPrice;

    private Integer quantity;

    private Double valued;
}
