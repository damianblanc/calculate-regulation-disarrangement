package com.bymatech.calculateregulationdisarrangement.dto;

import lombok.Data;

/**
 * Byma Cedears http response bean
 */
@Data
public class BymaCedearResponse {
    private Double tradeVolume;
    private String symbol;
    private Double imbalance;
    private Double previousSettlementPrice;
    private Double offerPrice;
    private Double vwap;
    private Integer numberOfOrders;
    private Double openingPrice;
    private String securityDesc;
    private String securitySubType;
    private Double previousClosingPrice;
    private Integer settlementType;
    private Integer quantityOffer;
    private Double tradingHighPrice;
    private String denominationCcy;
    private Double bidPrice;
    private Double tradingLowPrice;
    private String market;
    private Double volumeAmount;
    private Double volume;
    private Double trade;
    private String securityType;
    private Double closingPrice;
    private Double settlementPrice;
    private Double quantityBid;
}
