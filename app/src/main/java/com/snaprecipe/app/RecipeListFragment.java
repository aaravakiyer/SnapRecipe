package com.snaprecipe.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A Fragment that displays a list of recipes fetched from the API.
 */
public class RecipeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private RecipeViewModel recipeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize and set the adapter
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference to the RecipeViewModel.
        // Using requireActivity() ensures it shares the same ViewModel instance
        // with IngredientListFragment and survives fragment transactions.
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);

        // Observe the LiveData from the ViewModel.
        // When the recipe list changes, the observer is triggered and the UI is updated.
        recipeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                // Update the adapter with the new list of recipes
                adapter.setRecipes(recipes);
            }
        });
    }
}
