package com.bymatech.calculateregulationdisarrangement.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Byma Cedears http response bean
 */
@Data
public class MarketCedearResponse extends MarketResponse implements Comparable<MarketCedearResponse> {
    @SerializedName("tradeVolume")
    @Expose
    private Double tradeVolume;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("imbalance")
    @Expose
    private Double imbalance;
    @SerializedName("previousSettlementPrice")
    @Expose
    private Double previousSettlementPrice;
    @SerializedName("offerPrice")
    @Expose
    private Double offerPrice;
    @SerializedName("vwap")
    @Expose
    private Double vwap;
    @SerializedName("numberOfOrders")
    @Expose
    private Integer numberOfOrders;
    @SerializedName("openingPrice")
    @Expose
    private Double openingPrice;
    @SerializedName("securityDesc")
    @Expose
    private String securityDesc;
    @SerializedName("securitySubType")
    @Expose
    private String securitySubType;
    @SerializedName("previousClosingPrice")
    @Expose
    private Double previousClosingPrice;
    @SerializedName("settlementType")
    @Expose
    private Integer settlementType;
    @SerializedName("quantityOffer")
    @Expose
    private Integer quantityOffer;
    @SerializedName("tradingHighPrice")
    @Expose
    private Double tradingHighPrice;
    @SerializedName("denominationCcy")
    @Expose
    private String denominationCcy;
    @SerializedName("bidPrice")
    @Expose
    private Double bidPrice;
    @SerializedName("tradingLowPrice")
    @Expose
    private Double tradingLowPrice;
    @SerializedName("market")
    @Expose
    private String market;
    @SerializedName("volumeAmount")
    @Expose
    private Double volumeAmount;
    @SerializedName("volume")
    @Expose
    private Double volume;
    @SerializedName("trade")
    @Expose
    private Double trade;
    @SerializedName("securityType")
    @Expose
    private String securityType;
    @SerializedName("closingPrice")
    @Expose
    private Double closingPrice;
    @SerializedName("settlementPrice")
    @Expose
    private Double settlementPrice;
    @SerializedName("quantityBid")
    @Expose
    private Double quantityBid;

    @SerializedName("content")
    @Expose
    private MarketBondResponse.Content content;

    @SerializedName("data")
    @Expose
    private List<MarketCedearResponse> marketCedearResponses = new ArrayList<>();

    public static MarketCedearResponse create() { return new MarketCedearResponse(); }

    public void setSymbol(String symbol) {
        super.marketSymbol = symbol;
    }

    public void setTrade(Double trade) {
        this.trade = trade;
        super.marketPrice = String.valueOf(trade);
    }

    public void setFciSpecieType() {
        super.setFciSpecieType("Cedear");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketCedearResponse r)) return false;
        return Objects.equals(getSymbol(), r.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSymbol());
    }

    @Override
    public int compareTo(MarketCedearResponse other) {
        return symbol.compareTo(other.symbol);
    }
}
