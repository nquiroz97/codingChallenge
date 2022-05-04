package com.datechnologies.androidtest.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClientService {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://dev.rapptrlabs.com/Tests/scripts/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
