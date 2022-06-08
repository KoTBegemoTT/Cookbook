package com.example.cookbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.App;
import com.example.cookbook.MainActivity;
import com.example.cookbook.R;
import com.example.cookbook.api.CookBookService;
import com.example.cookbook.api.models.Category;
import com.example.cookbook.api.models.Preview;
import com.example.cookbook.api.models.api_classes.Relation;
import com.example.cookbook.api.models.api_classes.UserDishRequest;
import com.example.cookbook.models.Dish;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.DishViewHolder>{

    Context context;
    ArrayList<Preview> previews = new ArrayList<>();
    ImageLoader imageLoader;
    int layout;
    App app;


    public PreviewAdapter(Context context, int layout) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        this.layout = layout;
        app = (App)((Activity) context).getApplication();
    }

    public void setPreviews(List<Preview> previews) {
        this.previews.clear();
        this.previews.addAll(previews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dashItem = LayoutInflater.from(context).inflate(layout, parent, false);
        DishViewHolder dishViewHolder = new PreviewAdapter.DishViewHolder(dashItem);
        return dishViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        String imageUrl = CookBookService.BASE_URL + previews.get(position).getImageUrl();
        imageLoader.displayImage(imageUrl, holder.previewImage);
        holder.dishTitle.setText(previews.get(position).getName());
        holder.dishIngredientsCount.setText("Ингридиенты: " + previews.get(position).getIngredients());
        holder.dishStepsCount.setText("Шаги: " + previews.get(position).getSteps());

        holder.dishRatingBar.setRating(Float.parseFloat(previews.get(position).getRating()));

        String dishID = previews.get(position).getDish();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("DISH_ID", dishID);
                NavController navController = Navigation.findNavController((Activity) context,
                        R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.recipe, bundle);
            }
        });

        if (layout == R.layout.dish_item_with_cross) {
            holder.setDelete();
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendDeleteRequest(dishID, new Relation(false));
                    previews.remove(holder.getLayoutPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return previews.size();
    }

    public void sendDeleteRequest(String dishID, Relation relation){
        app.getCookBookService().getApi().updateRelation(dishID, new UserDishRequest(relation))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    public static final class DishViewHolder extends RecyclerView.ViewHolder {

        ImageView previewImage;
        TextView dishTitle, dishIngredientsCount, dishStepsCount;
        RatingBar dishRatingBar;
        ImageButton deleteButton;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            previewImage = itemView.findViewById(R.id.dishImage);
            dishTitle = itemView.findViewById(R.id.dishTitle);
            dishIngredientsCount = itemView.findViewById(R.id.dishIngredientsCount);
            dishStepsCount = itemView.findViewById(R.id.dishStepsCount);
            dishRatingBar = itemView.findViewById(R.id.dishRatingBar);
        }

        public void setDelete() {
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
