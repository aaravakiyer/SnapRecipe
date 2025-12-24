package com.snaprecipe.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CO2DashboardFragment extends Fragment {
    private TextView tvCO2Saved;
    private IngredientViewModel ingredientViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_co2_dashboard, container, false);

        tvCO2Saved = view.findViewById(R.id.tv_co2_saved);

        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        ingredientViewModel.getTotalCO2Saved().observe(getViewLifecycleOwner(), total -> {
            if (total == null) total = 0.0;
            tvCO2Saved.setText(String.format("%.2f kg CO₂", total));
        });

        return view;
    }
}
