package com.bymatech.calculateregulationdisarrangement.service.http;

import com.bymatech.calculateregulationdisarrangement.dto.MarketCedearResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

public interface    PrimaryHttpService {

    @Headers("")
    @GET("/rest/instruments/details")
    Call<List<MarketCedearResponse>> getAllSpecies();
}
