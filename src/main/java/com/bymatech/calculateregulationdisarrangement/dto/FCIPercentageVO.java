package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FCIPercentageVO {

    private Integer id;
    private String specieType;
    private String percentage;
}
