package com.example.cookbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.api.CookBookService;
import com.example.cookbook.api.models.Category;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories = new ArrayList<>();
    ImageLoader imageLoader;


    public CategoryAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    public void setCategories(List<Category> cat) {
        this.categories.clear();
        this.categories.addAll(cat);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItem = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.CategoryViewHolder(categoryItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String title = categories.get(position).getName();
        holder.categoryTitle.setText(title);

        String imageUrl = CookBookService.BASE_URL + categories.get(position).getImageUrl();
        imageLoader.displayImage(imageUrl, holder.categoryImage);

        String id = categories.get(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("CATEGORY_ID", id);
                NavController navController = Navigation.findNavController((Activity) context,
                        R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.category, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTitle;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
