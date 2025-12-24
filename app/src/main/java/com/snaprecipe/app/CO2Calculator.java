package com.snaprecipe.app;

import java.util.HashMap;
import java.util.Map;

public class CO2Calculator {
    private static final Map<String, Double> CO2_VALUES = new HashMap<>();

    static {
        CO2_VALUES.put("tomato", 1.1);
        CO2_VALUES.put("carrot", 0.4);
        CO2_VALUES.put("potato", 0.3);
        CO2_VALUES.put("onion", 0.3);
        CO2_VALUES.put("apple", 0.5);
        CO2_VALUES.put("banana", 0.7);
        CO2_VALUES.put("lettuce", 0.2);
        CO2_VALUES.put("broccoli", 2.0);
        CO2_VALUES.put("pepper", 0.6);
        CO2_VALUES.put("cucumber", 0.3);
        CO2_VALUES.put("default", 0.5);
    }

    public static double calculateCO2Saved(String ingredientName) {
        String lowerName = ingredientName.toLowerCase();
        for (Map.Entry<String, Double> entry : CO2_VALUES.entrySet()) {
            if (lowerName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return CO2_VALUES.get("default");
    }
}