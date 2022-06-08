package com.example.cookbook.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    Context context;
    String[] ingredients = new String[]{};
    String[] amount = new String[]{};

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    public void setIngredients(String[] ingredients, String[] amount){
        this.ingredients = ingredients;
        this.amount = amount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingredientItem = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientAdapter.IngredientViewHolder(ingredientItem);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.ingredientTitle.setText(ingredients[position].trim());
        holder.ingredientAmount.setText(amount[position].trim());
    }

    public String getIngredientAmounts() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < ingredients.length; i++){
            text.append(ingredients[i]);
            text.append(amount[i]);
            text.append(", ");
        }
        return text.toString();
    }

    @Override
    public int getItemCount() {
        return ingredients.length;
    }

    public static final class IngredientViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTitle, ingredientAmount;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientTitle = itemView.findViewById(R.id.ingredient_title);
            ingredientAmount = itemView.findViewById(R.id.ingredient_amount);
        }
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
