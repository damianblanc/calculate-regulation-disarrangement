package com.bymatech.calculateregulationdisarrangement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

 /**
 * https://open.bymadata.com.ar/vanoms-be-core/rest/api/bymadata/free/leading-equity
 * Used Bean for Market Http Requests
 *  {
 *   "excludeZeroPxAndQty": true,
 *   "T2": true,
 *   "T1": false,
 *   "T0": false,
 *   "Content-Type": "application/json"
 *  }
 */
@Getter
@AllArgsConstructor
public class MarketEquityAuthBean {

    @SerializedName("page_number")
    @Expose
    private Integer pageNumber = 0;
    private boolean excludeZeroPxAndQty = true;
    private boolean T1 = false;
    private boolean T0 = false;
    @JsonProperty("Content_Type")
    private String contentType = "application/json";

    public static MarketEquityAuthBean create(Integer pageNumber) {
        return new MarketEquityAuthBean(pageNumber, true, true, false, "\"application/json\"");
    }
}
