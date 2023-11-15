package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FCIPercentageAndValuedVO {
    private Integer id;
    private String specieType;
    private String percentage;
    private String valued;

    private String rpercentage;
    private String rvalued;
}
