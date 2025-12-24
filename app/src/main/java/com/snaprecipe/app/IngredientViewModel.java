package com.snaprecipe.app;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository repository;
    private LiveData<List<Ingredient>> allIngredients;
    private LiveData<Double> totalCO2Saved;

    public IngredientViewModel(@NonNull Application application) {
        super(application);
        repository = new IngredientRepository(application);
        allIngredients = repository.getAllIngredients();
        totalCO2Saved = repository.getTotalCO2Saved();
    }

    public void insert(Ingredient ingredient) {
        repository.insert(ingredient);
    }

    public void update(Ingredient ingredient) {
        repository.update(ingredient);
    }

    public void delete(Ingredient ingredient) {
        repository.delete(ingredient);
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return allIngredients;
    }

    public LiveData<Double> getTotalCO2Saved() {
        return totalCO2Saved;
    }
}