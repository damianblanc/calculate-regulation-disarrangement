package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.BymaCedearResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

public interface PrimaryHttpService {

    @Headers("")
    @GET("/rest/instruments/details")
    Call<List<BymaCedearResponse>> getAllSpecies();
}
