package com.bymatech.calculateregulationdisarrangement.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * https://open.bymadata.com.ar/vanoms-be-core/rest/api/bymadata/free/bnown/seriesHistoricas/iamc/bonos
 * {"page_number":1,"Content-Type":"application/json"}
 */
@Getter
@AllArgsConstructor
public class MarketBondAuthBean {
    @SerializedName("page_number")
    @Expose
    private Integer pageNumber = 0;

    @SerializedName("Content_Type")
    @Expose
    private String contentType = "application/json";

    public static MarketBondAuthBean create(Integer pageNumber) {
       return new MarketBondAuthBean(pageNumber, "\"application/json\"");
    }
}
