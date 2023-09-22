package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeciePosition {

    private String name;
    private String symbol;
    private SpecieType specieType;
    private Double price;
    private Integer quantity;

    public Double valuePosition() {
        return price * quantity;
    }

}
