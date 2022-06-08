//package com.example.cookbook;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.example.cookbook.adapters.CategoryAdapter;
//import com.example.cookbook.adapters.DishAdapter;
//import com.example.cookbook.models.Category;
//import com.example.cookbook.models.Dish;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    RecyclerView categoryRecycler, dishRecycler;
//    CategoryAdapter categoryAdapter;
//    DishAdapter dishAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        List<Category> categories = new ArrayList<>();
//        categories.add(new Category("soup", "Супы"));
//        categories.add(new Category("second", "Второе"));
//        categories.add(new Category("salat", "Салаты"));
//        categories.add(new Category("salat", "Салаты"));
//        categories.add(new Category("salat", "Салаты"));
//
//        setCategoryRecycler(categories);
//
//        List<Dish> dishes = new ArrayList<>();
//        dishes.add(new Dish("borsch", "Борщ со свининой", "12", "8",  4f));
//        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
//        dishes.add(new Dish("borsch", "Борщ со свининой", "12", "8",  4f));
//        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
//        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
//        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
//        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
//
//        setDishRecycler(dishes);
//    }
//
//    private void setDishRecycler(List<Dish> dishes) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        dishRecycler = findViewById(R.id.dishRecycler);
//        dishRecycler.setLayoutManager(layoutManager);
//
//        dishAdapter = new DishAdapter(this, dishes);
//        dishRecycler.setAdapter(dishAdapter);
//    }
//
//    private void setCategoryRecycler(List<Category> categories) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        categoryRecycler = findViewById(R.id.categoryRecycler);
//        categoryRecycler.setLayoutManager(layoutManager);
//
//        categoryAdapter = new CategoryAdapter(this, categories);
//        categoryRecycler.setAdapter(categoryAdapter);
//    }
//
//    public void scrollRight(View view){
//        LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();
//        categoryRecycler.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition() + 1);
//    }
//
//    public void scrollLeft(View view){
//        LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();
//        int target = layoutManager.findFirstVisibleItemPosition() - 1;
//        if (target < 0) target = 0;
//        categoryRecycler.smoothScrollToPosition(target);
//    }
//}