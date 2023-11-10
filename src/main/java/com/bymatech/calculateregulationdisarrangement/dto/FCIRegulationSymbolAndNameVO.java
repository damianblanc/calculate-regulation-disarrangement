package com.bymatech.calculateregulationdisarrangement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FCIRegulationSymbolAndNameVO {

    private Integer id;
    private String fciSymbol;
    private String fciName;
}
