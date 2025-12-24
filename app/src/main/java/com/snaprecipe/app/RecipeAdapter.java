package com.snaprecipe.app;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes = new ArrayList<>();

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvMatchInfo.setText("Uses " + recipe.getUsedIngredientCount() +
                " of your ingredients • Needs " + recipe.getMissedIngredientCount() + " more");

        Glide.with(holder.itemView.getContext())
                .load(recipe.getIngredients())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivRecipe);

        holder.itemView.setOnClickListener(v -> {
            String url = "https://spoonacular.com/recipes/-" + recipe.getId();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecipe;
        TextView tvTitle, tvMatchInfo;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ivRecipe = itemView.findViewById(R.id.iv_recipe);
            tvTitle = itemView.findViewById(R.id.tv_recipe_title);
            tvMatchInfo = itemView.findViewById(R.id.tv_recipe_match);
        }
    }
}