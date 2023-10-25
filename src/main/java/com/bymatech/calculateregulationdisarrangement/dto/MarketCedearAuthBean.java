package com.bymatech.calculateregulationdisarrangement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Used Bean used in Byma Http Requests
 *  {
 *         "excludeZeroPxAndQty": true,
 *             "T2": true,
 *             "T1": false,
 *             "T0": false,
 *             "Content-Type": "application/json"
 */
@Getter
public class MarketCedearAuthBean {

    private boolean excludeZeroPxAndQty = true;
    private boolean T2 = true;
    private boolean T1 = false;
    private boolean t0 = false;
    @JsonProperty("Content_Type")
    private String contentType = "application/json";

    public static MarketCedearAuthBean create() {
        return new MarketCedearAuthBean();
    }
}
