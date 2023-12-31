package com.bymatech.calculateregulationdisarrangement.service.http;

import com.bymatech.calculateregulationdisarrangement.dto.*;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

@Service
public interface BymaHttpService {
        @POST("/vanoms-be-core/rest/api/bymadata/free/cedears")
        Call<List<MarketCedearResponse>> getCedears(@Body MarketCedearAuthBean marketCedearAuthBean);

        @POST("vanoms-be-core/rest/api/bymadata/free/bnown/seriesHistoricas/iamc/bonos")
        Call<MarketBondResponse> getBonds(@Body MarketBondAuthBean marketBondAuthBean);

        @POST("vanoms-be-core/rest/api/bymadata/free/leading-equity")
        Call<MarketEquityResponse> getLeadingEquities(@Body MarketEquityAuthBean marketEquityAuthBean);

        @POST("vanoms-be-core/rest/api/bymadata/free/general-equity")
        Call<MarketEquityResponse> getGeneralEquities(@Body MarketEquityAuthBean marketEquityAuthBean);

        @POST("https://open.bymadata.com.ar/vanoms-be-core/rest/api/bymadata/free/market-time")
        Call<MarketTimeResponse> getMarketTime();
}
