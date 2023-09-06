package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Data;

@Data
public class SpeciePosition {

    private String name;
    private SpecieType specieType;
    private Double price;
    private Integer quantity;

    public Double valuePosition() {
        return price * quantity;
    }

}
