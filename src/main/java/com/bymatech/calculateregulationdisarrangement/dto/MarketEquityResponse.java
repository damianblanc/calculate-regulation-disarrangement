package com.bymatech.calculateregulationdisarrangement.dto;

import com.bymatech.calculateregulationdisarrangement.dto.MarketBondResponse.MarketBondResponseElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {
 *     "content": {
 *         "page_number": 1,
 *         "page_count": 1,
 *         "page_size": 50,
 *         "total_elements_count": 20
 *     },
 *     "data": [
 *         {
 *             "tradeVolume": 578876,
 *             "symbol": "ALUA",
 *             "imbalance": 0.0255,
 *             "previousSettlementPrice": 605.5,
 *             "offerPrice": 641,
 *             "openInterest": 0,
 *             "vwap": 613.3957937,
 *             "numberOfOrders": 0,
 *             "openingPrice": 605,
 *             "tickDirection": 2,
 *             "securityDesc": "",
 *             "securitySubType": "M",
 *             "previousClosingPrice": 605.5,
 *             "settlementType": "3",
 *             "quantityOffer": 10,
 *             "tradingHighPrice": 625,
 *             "denominationCcy": "ARS",
 *             "bidPrice": 610,
 *             "tradingLowPrice": 590,
 *             "market": "BYMA",
 *             "volumeAmount": 357107697,
 *             "volume": 578876,
 *             "trade": 621,
 *             "securityType": "CS",
 *             "closingPrice": 619.5,
 *             "settlementPrice": 0E-8,
 *             "quantityBid": 86
 *         }
 * }
 */
@Data
public class MarketEquityResponse {
    @SerializedName("content")
    @Expose
    private MarketBondResponse.Content content;

    @SerializedName("data")
    @Expose
    private List<MarketEquityResponseElement> marketEquityResponses = new ArrayList<>();


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

    public static MarketEquityResponse create() { return new MarketEquityResponse(); }

    /**
     * Byma bonds Http Response
     */
    @Data
    public static class MarketEquityResponseElement extends MarketResponse implements Comparable<MarketEquityResponseElement>, Market {
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
        @SerializedName("openInterest")
        @Expose
        private String openInterest;
        @SerializedName("vwap")
        @Expose
        private String vwap;
        @SerializedName("numberOfOrders")
        @Expose
        private String numberOfOrders;
        @SerializedName("openingPrice")
        @Expose
        private String openingPrice;
        @SerializedName("tickDirection")
        @Expose
        private String tickDirection;
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
            super.setFciSpecieType("Equity");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MarketEquityResponse.MarketEquityResponseElement r)) return false;
            return Objects.equals(getSymbol(), r.getSymbol());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSymbol());
        }

        @Override
        public int compareTo(@NotNull MarketEquityResponseElement e) {
            return this.getSymbol().compareTo(e.getSymbol());
        }
    }
}
