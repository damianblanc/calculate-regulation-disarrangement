package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.dto.MarketBondResponse.Content;
import com.bymatech.calculateregulationdisarrangement.dto.MarketBondResponse.MarketBondResponseElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Byma Cedears http response bean
 */
@Data
public class MarketCedearResponse {
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

    @SerializedName("content")
    @Expose
    private MarketBondResponse.Content content;

    @SerializedName("data")
    @Expose
    private List<MarketCedearResponseElement> marketCedearResponses = new ArrayList<>();

    @Getter
    public class Content {
        @SerializedName("page_number")
        @Expose
        private Integer pageNumber = 0;

        @SerializedName("page_count")
        @Expose
        private Integer pageCount;

        @SerializedName("page_size")
        @Expose
        private Integer pageSize = 1;

        @SerializedName("total_elements_count")
        @Expose
        private Integer totalElementsCount;
    }

    public static MarketCedearResponse create() { return new MarketCedearResponse(); }

    /**
     * Byma cedear Http Response
     */
    @Data
    public class MarketCedearResponseElement extends MarketResponse
        implements Comparable<MarketCedearResponse.MarketCedearResponseElement> {

        @SerializedName("tradeVolume")
        @Expose
        private String tradeVolume;
        @SerializedName("symbol")
        @Expose
        private String symbol;
        @SerializedName("imbalance")
        @Expose
        private String imbalance;
        @SerializedName("previousSettlementPrice")
        @Expose
        private String previousSettlementPrice;
        @SerializedName("offerPrice")
        @Expose
        private String offerPrice;
        @SerializedName("vwap")
        @Expose
        private String vwap;
        @SerializedName("numberOfOrders")
        @Expose
        private String numberOfOrders;
        @SerializedName("openingPrice")
        @Expose
        private String openingPrice;
        @SerializedName("securityDesc")
        @Expose
        private String securityDesc;
        @SerializedName("securitySubType")
        @Expose
        private String securitySubType;
        @SerializedName("previousClosingPrice")
        @Expose
        private String previousClosingPrice;
        @SerializedName("settlementType")
        @Expose
        private String settlementType;
        @SerializedName("quantityOffer")
        @Expose
        private String quantityOffer;
        @SerializedName("tradingHighPrice")
        @Expose
        private String tradingHighPrice;
        @SerializedName("denominationCcy")
        @Expose
        private String denominationCcy;
        @SerializedName("bidPrice")
        @Expose
        private String bidPrice;
        @SerializedName("tradingLowPrice")
        @Expose
        private String tradingLowPrice;
        @SerializedName("market")
        @Expose
        private String market;
        @SerializedName("volumeAmount")
        @Expose
        private String volumeAmount;
        @SerializedName("volume")
        @Expose
        private String volume;
        @SerializedName("trade")
        @Expose
        private String trade;
        @SerializedName("securityType")
        @Expose
        private String securityType;
        @SerializedName("closingPrice")
        @Expose
        private String closingPrice;
        @SerializedName("settlementPrice")
        @Expose
        private String settlementPrice;
        @SerializedName("quantityBid")
        @Expose
        private String quantityBid;

        public void setSymbol(String symbol) {
            super.marketSymbol = symbol;
        }

        public void setTrade(String trade) {
            this.trade = trade;
            super.marketPrice = trade;
        }

        public void setFciSpecieType() {
            super.setFciSpecieType("Cedear");
        }

        @Override
        public int compareTo(@NotNull MarketCedearResponse.MarketCedearResponseElement e) {
            if (Double.parseDouble(this.getTrade()) >= Double.parseDouble(e.getTrade())) {
                return 1;
            }
            return -1;
        }
    }
}
