package com.snaprecipe.app;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository repository;
    private LiveData<List<Recipe>> recipes; // Renamed from allRecipes
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // TODO: Replace with your actual Spoonacular API key
    private static final String SPOONACULAR_API_KEY = "c05733ec13384318bddd44c08e79d973";

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new RecipeRepository(application);
        recipes = repository.getAllRecipes(); // The repository method can keep its name
    }

    public void fetchRecipes(String ingredients) {
        SpoonacularApiService service = RetrofitClient.getSpoonacularClient()
                .create(SpoonacularApiService.class);

        Call<SpoonacularResponse> call = service.getRecipesByIngredients(
                ingredients, 20, SPOONACULAR_API_KEY
        );

        call.enqueue(new Callback<SpoonacularResponse>() {
            @Override
            public void onResponse(Call<SpoonacularResponse> call, Response<SpoonacularResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> fetchedRecipes = new ArrayList<>(); // Renamed for clarity
                    for (SpoonacularResponse.RecipeResult result : response.body()) {
                        fetchedRecipes.add(new Recipe(
                                result.id,
                                result.title,
                                result.image,
                                result.usedIngredientCount,
                                result.missedIngredientCount
                        ));
                    }
                    repository.clearRecipes();
                    repository.insertAll(fetchedRecipes);
                } else {
                    errorMessage.postValue("Failed to fetch recipes");
                }
            }

            @Override
            public void onFailure(Call<SpoonacularResponse> call, Throwable t) {
                errorMessage.postValue("Network error: " + t.getMessage());
            }
        });
    }

    // Renamed this method from getAllRecipes to getRecipes
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
