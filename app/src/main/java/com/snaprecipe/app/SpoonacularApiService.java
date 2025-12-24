package com.snaprecipe.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularApiService {
    @GET("recipes/findByIngredients")
    Call<SpoonacularResponse> getRecipesByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );
}