package com.snaprecipe.app;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class SpoonacularResponse extends ArrayList<SpoonacularResponse.RecipeResult> {
    public static class RecipeResult {
        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("usedIngredientCount")
        public int usedIngredientCount;
        @SerializedName("missedIngredientCount")
        public int missedIngredientCount;
    }
}
