package com.snaprecipe.app;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> allRecipes;
    private ExecutorService executorService;

    public RecipeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertAll(List<Recipe> recipes) {
        executorService.execute(() -> recipeDao.insertAll(recipes));
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public void clearRecipes() {
        executorService.execute(() -> recipeDao.deleteAll());
    }
}