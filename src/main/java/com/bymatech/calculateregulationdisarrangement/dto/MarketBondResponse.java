package com.bymatech.calculateregulationdisarrangement.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Byma bonds Http Response
 * Wrapper class composed of a header content + list of BymaBondResponse elements
 *
 *  "content": {
 *         "page_number": 1,
 *         "page_count": 2,
 *         "page_size": 50,
 *         "total_elements_count": 84
 *     },
 *     "data": [
 *         {
 *             "descripcion": "BONO DE CONS (PROV.) 6TA. SER. EN $ 2%",
 *             "symbol": "PR13",
 *             "hora": "03:45",
 *             "notas": "",
 *             "paridad": 1.102138,
 *             "fechaCot": "2023-09-19",
 *             "dm": 0.306471,
 *             "tirAnual": -0.265407,
 *             "cotizacion": 609,
 *             "vTecnico": 558.006559,
 *             "vr": 5.38,
 *             "intCorr": 0.154959,
 *             "rentaAnual": "Fija=2",
 *             "isin": "ARARGE03B291"
 *         }
 *          ],
 *     "empty": false,
 *     "upgrade": false
 * }
 *
 */
@Data
@NoArgsConstructor
public class MarketBondResponse implements Market {

    @SerializedName("content")
    @Expose
    private Content content;

    @SerializedName("data")
    @Expose
    private List<MarketBondResponseElement> marketBondResponses = new ArrayList<>();


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

    public static MarketBondResponse create() { return new MarketBondResponse(); }

    /**
     * Byma bonds Http Response
     */
    @Data
    public static class MarketBondResponseElement extends MarketResponse implements Comparable<MarketBondResponseElement>, Market {

        @SerializedName("descripcion")
        @Expose
        private String description;
        @SerializedName("symbol")
        @Expose
        private String symbol;
        @SerializedName("hora")
        @Expose
        private String time;
        @SerializedName("notas")
        @Expose
        private String notes;
        @SerializedName("paridad")
        @Expose
        private String parity;
        @SerializedName("fechaCat")
        @Expose
        private String catDate;
        @SerializedName("dm")
        @Expose
        private String dm;
        @SerializedName("tirAnual")
        @Expose
        private String AnnualTIR;
        @SerializedName("cotizacion")
        @Expose
        private String price;
        @SerializedName("vTecnico")
        @Expose
        private String techValue;
        @SerializedName("vr")
        @Expose
        private String vr;
        @SerializedName("intCorr")
        @Expose
        private String intCorr;
        @SerializedName("rentaAnual")
        @Expose
        private String annualRent;
        @SerializedName("isin")
        @Expose
        private String isin;

        public void setSymbol(String symbol) {
            super.marketSymbol = symbol;
        }

        public void setPrice(String price) {
            this.price = price;
            super.marketPrice = price;
        }

        public void setFciSpecieType() {
            super.setFciSpecieType("Bond");  //TODO: This has to be bound to correct specie type once created relation in model (getting species from market,
                                             //TODO: allowing to relate them to created specie types)
        }

        @Override
        public int compareTo(@NotNull MarketBondResponseElement e) {
            if (Double.parseDouble(this.getMarketPrice()) >= Double.parseDouble(e.getMarketPrice())) {
                return 1;
            }
            return -1;
        }
    }
}
