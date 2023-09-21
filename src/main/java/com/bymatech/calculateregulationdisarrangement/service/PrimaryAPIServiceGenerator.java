package com.bymatech.calculateregulationdisarrangement.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrimaryAPIServiceGenerator {

    private static final String BASE_URL = "https://api.remarkets.primary.com.ar";

    private static Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

}
