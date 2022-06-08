package com.example.cookbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.api.CookBookService;
import com.example.cookbook.models.RecipeStep;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    Context context;
    ArrayList<RecipeStep> steps = new ArrayList<>();
    ImageLoader imageLoader;

    public RecipeStepAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    public void setSteps(List<RecipeStep> steps){
        this.steps.addAll(steps);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeStepItem = LayoutInflater.from(context).inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepAdapter.RecipeStepViewHolder(recipeStepItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        holder.stepDescription.setText(steps.get(position).getStepDescription());
        int stepNumber = position + 1;
        holder.stepNumber.setText("Шаг " + stepNumber);

        String imageUrl = CookBookService.BASE_URL + steps.get(position).getStepImage();
        imageLoader.displayImage(imageUrl, holder.stepImage);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public static final class RecipeStepViewHolder extends RecyclerView.ViewHolder{

        TextView stepDescription, stepNumber;
        ImageView stepImage;

        public RecipeStepViewHolder(@NonNull View itemView) {
            super(itemView);

            stepDescription = itemView.findViewById(R.id.recipe_step_description);
            stepNumber = itemView.findViewById(R.id.step_number);
            stepImage = itemView.findViewById(R.id.step_image);
        }
    }
}
