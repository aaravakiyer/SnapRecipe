package com.snaprecipe.app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM ingredients WHERE isUsed = 0 ORDER BY expiryDate ASC")
    LiveData<List<Ingredient>> getAllActiveIngredients();

    @Query("SELECT SUM(co2Saved) FROM ingredients WHERE isUsed = 1")
    LiveData<Double> getTotalCO2Saved();
}