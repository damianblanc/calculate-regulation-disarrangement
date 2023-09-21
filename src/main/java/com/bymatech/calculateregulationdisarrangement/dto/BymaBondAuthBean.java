package com.bymatech.calculateregulationdisarrangement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * https://open.bymadata.com.ar/vanoms-be-core/rest/api/bymadata/free/bnown/seriesHistoricas/iamc/bonos
 * {"page_number":1,"Content-Type":"application/json"}
 */
@Getter
public class BymaBondAuthBean {

    @JsonProperty("page_number")
    private Integer pageNumber = 1;


    @JsonProperty("Content_Type")
    private String contentType = "application/json";

    public static BymaBondAuthBean create() { return new BymaBondAuthBean(); }
}
