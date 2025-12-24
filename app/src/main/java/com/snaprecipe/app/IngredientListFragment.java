package com.snaprecipe.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private IngredientViewModel ingredientViewModel;
    private RecipeViewModel recipeViewModel;
    private Button btnFindRecipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_ingredients);
        btnFindRecipes = view.findViewById(R.id.btn_find_recipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IngredientAdapter();
        recyclerView.setAdapter(adapter);

        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        ingredientViewModel.getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            adapter.setIngredients(ingredients);
        });

        adapter.setOnDeleteClickListener(ingredient -> {
            ingredientViewModel.delete(ingredient);
        });

        btnFindRecipes.setOnClickListener(v -> findRecipes());

        return view;
    }

    private void findRecipes() {
        if (adapter.getItemCount() == 0) {
            Toast.makeText(getContext(), "Add ingredients first!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder ingredientList = new StringBuilder();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (i > 0) ingredientList.append(",");
            ingredientList.append(adapter.getIngredients().get(i).getName());
        }

        recipeViewModel.fetchRecipes(ingredientList.toString());
        Toast.makeText(getContext(), "Searching for recipes...", Toast.LENGTH_SHORT).show();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RecipeListFragment())
                .commit();
    }
}