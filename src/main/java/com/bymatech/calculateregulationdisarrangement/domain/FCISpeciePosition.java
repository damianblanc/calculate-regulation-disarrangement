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

    private String name;
    private String symbol;
    private FCISpecieType fciSpecieType;

    /**
     * This property is updated each time a FCIPosition is retrieved to work with market price
     */
    @Transient
    private Double currentMarketPrice;
    private Integer quantity;

    public Double valuePosition() {
        return currentMarketPrice * quantity;
    }

}
