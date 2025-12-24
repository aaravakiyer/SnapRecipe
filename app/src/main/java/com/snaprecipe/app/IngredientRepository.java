package com.snaprecipe.app;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IngredientRepository {
    private IngredientDao ingredientDao;
    private LiveData<List<Ingredient>> allIngredients;
    private LiveData<Double> totalCO2Saved;
    private ExecutorService executorService;

    public IngredientRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        ingredientDao = database.ingredientDao();
        allIngredients = ingredientDao.getAllActiveIngredients();
        totalCO2Saved = ingredientDao.getTotalCO2Saved();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Ingredient ingredient) {
        executorService.execute(() -> ingredientDao.insert(ingredient));
    }

    public void update(Ingredient ingredient) {
        executorService.execute(() -> ingredientDao.update(ingredient));
    }

    public void delete(Ingredient ingredient) {
        executorService.execute(() -> ingredientDao.delete(ingredient));
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return allIngredients;
    }

    public LiveData<Double> getTotalCO2Saved() {
        return totalCO2Saved;
    }
}