package com.snaprecipe.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private long expiryDate;
    private boolean isUsed;
    private double co2Saved;

    public Ingredient(String name, long expiryDate) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.isUsed = false;
        this.co2Saved = 0.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public long getExpiryDate() { return expiryDate; }
    public void setExpiryDate(long expiryDate) { this.expiryDate = expiryDate; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
    public double getCo2Saved() { return co2Saved; }
    public void setCo2Saved(double co2Saved) { this.co2Saved = co2Saved; }
}