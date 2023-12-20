package com.bymatech.calculateregulationdisarrangement.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {
 *     "isWorkingDay": true,
 *     "marketClosingTime": "17:00:00",
 *     "timezone": "GMT-03:00",
 *     "marketOpeningTime": "11:00:00"
 * }
 */
public class MarketTimeResponse {

    @SerializedName("content")
    @Expose
    private Boolean isWorkingDay;

    @SerializedName("content")
    @Expose
    private String marketClosingTime;

    @SerializedName("content")
    @Expose
    private String timezone;

    @SerializedName("content")
    @Expose
    private String marketOpeningTime;
}
