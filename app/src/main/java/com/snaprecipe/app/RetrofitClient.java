package com.snaprecipe.app;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String SPOONACULAR_BASE_URL = "https://api.spoonacular.com/";
    private static Retrofit retrofitSpoonacular;

    public static Retrofit getSpoonacularClient() {
        if (retrofitSpoonacular == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofitSpoonacular = new Retrofit.Builder()
                    .baseUrl(SPOONACULAR_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitSpoonacular;
    }
}