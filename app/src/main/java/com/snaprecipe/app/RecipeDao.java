package com.snaprecipe.app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Recipe> recipes);

    @Query("SELECT * FROM recipes ORDER BY usedIngredientCount DESC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("DELETE FROM recipes")
    void deleteAll();
}