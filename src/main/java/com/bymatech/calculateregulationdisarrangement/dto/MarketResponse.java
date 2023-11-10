package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.Data;

/**
 * Marker class to gather common responses to different properties
 */
@Data
public abstract class MarketResponse {

    protected String marketPrice;
    protected String marketSymbol;

    protected String fciSpecieType;

}
