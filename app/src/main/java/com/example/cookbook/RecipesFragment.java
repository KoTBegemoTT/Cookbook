package com.example.cookbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.adapters.CategoryAdapter;
import com.example.cookbook.adapters.PreviewAdapter;
import com.example.cookbook.api.models.Preview;
import com.example.cookbook.databinding.FragmentRecipesBinding;
import com.example.cookbook.api.models.Category;
import com.example.cookbook.models.Dish;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class RecipesFragment extends Fragment {

    RecyclerView categoryRecycler, previewRecycler;
    CategoryAdapter categoryAdapter;
    PreviewAdapter previewAdapter;

    private Context context;
    private FragmentRecipesBinding binding;
    public App app;

    CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button categoriesButton = root.findViewById(R.id.categoriesButton);
        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController((Activity) context,
                        R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_categories);
            }
        });
        setResources(root);
        return root;
    }

    private void setResources(View root) {
        setCategoryRecycler(root);
        disposable.add(app.getCookBookService().getApi().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<Category>, Throwable>() {
                    @Override
                    public void accept(List<Category> dates, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", "++++++++++++++++++++++++++++++++++++++++++++++++++++T");
                            Log.i("T", throwable.getMessage());
                        } else {
                            categoryAdapter.setCategories(dates);
                        }
                    }
                }));

        setPreviewRecycler(root);
        disposable.add(app.getCookBookService().getApi().getPreviews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<Preview>, Throwable>() {
                    @Override
                    public void accept(List<Preview> dates, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", "++++++++++++++++++++++++++++++++++++++++++++++++++++T");
                            Log.i("T", throwable.getMessage());
                        } else {
                            previewAdapter.setPreviews(dates);
                        }
                    }
                }));

        setButtons(root);
    }

    private void setButtons(View root) {
        ImageButton buttonLeft = root.findViewById(R.id.arrowLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();
                int target = layoutManager.findFirstVisibleItemPosition() - 1;
                if (target < 0) target = 0;
                categoryRecycler.smoothScrollToPosition(target);
            }
        });

        ImageButton buttonRight = root.findViewById(R.id.arrowRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();
                categoryRecycler.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition() + 1);
            }
        });
    }

    private void setPreviewRecycler(View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        previewRecycler = root.findViewById(R.id.previewRecycler);
        previewRecycler.setLayoutManager(layoutManager);

        previewAdapter = new PreviewAdapter(context, R.layout.dish_item);
        previewRecycler.setAdapter(previewAdapter);
    }

    private void setCategoryRecycler(View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        categoryRecycler = root.findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(context);
        categoryRecycler.setAdapter(categoryAdapter);
    }


    @Override
    public void onDestroyView() {
//        disposable.dispose();
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        app = (App) activity.getApplication();
    }
}