package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents a Specie in a Position
 */
public class FCISpeciePosition {

    private String symbol;
    private String fciSpecieType;
    private String fciSpecieGroup;

    /**
     * This property is updated each time a FCIPosition is retrieved to work with market price
     */
    @Transient
    private Double currentMarketPrice = -1.0;
    private Integer quantity;

    public Double valueSpecieInPosition() {
        return currentMarketPrice * quantity;
    }

    /**
     * Each FCISpeciePosition is not aware of group lot definition, meaning that must be taken from specie indirectly binding to group
     */
    public Double valueSpecieInPosition(Integer lot) {
        return currentMarketPrice * quantity / lot;
    }
}
