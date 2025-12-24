package com.snaprecipe.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    private int id;
    private String title;
    // New field for the list of ingredients
    private String ingredients;
    private int usedIngredientCount;
    private int missedIngredientCount;

    public Recipe() {}

    public Recipe(int id, String title, String ingredients, int usedIngredientCount, int missedIngredientCount) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.usedIngredientCount = usedIngredientCount;
        this.missedIngredientCount = missedIngredientCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public int getUsedIngredientCount() { return usedIngredientCount; }
    public void setUsedIngredientCount(int count) { this.usedIngredientCount = count; }
    public int getMissedIngredientCount() { return missedIngredientCount; }
    public void setMissedIngredientCount(int count) { this.missedIngredientCount = count; }
}