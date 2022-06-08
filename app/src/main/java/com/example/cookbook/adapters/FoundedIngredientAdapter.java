package com.example.cookbook.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

public class FoundedIngredientAdapter extends RecyclerView.Adapter<FoundedIngredientAdapter.FoundedIngredientViewHolder> {

    Context context;
    String[] ingredients = new String[]{};

    public FoundedIngredientAdapter(Context context) {
        this.context = context;
    }

    public void setIngredients(String[] ingredients){
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoundedIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingredientItem = LayoutInflater.from(context).inflate(R.layout.founded_ingredient_item, parent, false);
        return new FoundedIngredientAdapter.FoundedIngredientViewHolder(ingredientItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundedIngredientViewHolder holder, int position) {
        holder.ingredientTitle.setText(ingredients[position].trim());
    }

    @Override
    public int getItemCount() {
        return ingredients.length;
    }

    public static final class FoundedIngredientViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTitle;

        public FoundedIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientTitle = itemView.findViewById(R.id.ingredient_title);
        }
    }
}
