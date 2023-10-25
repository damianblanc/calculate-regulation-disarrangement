package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.Getter;

/**
 * Marker class to gather common responses to different properties
 */
@Getter
public abstract class MarketResponse {

    protected String price;

    protected String symbol;

}
