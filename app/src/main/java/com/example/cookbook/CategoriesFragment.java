package com.example.cookbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.adapters.CategoryAdapter;
import com.example.cookbook.databinding.FragmentCategoriesBinding;
import com.example.cookbook.models.Category;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class CategoriesFragment extends Fragment {

    RecyclerView categoryRecycler;
    CategoryAdapter categoryAdapter;

    private Context context;
    private FragmentCategoriesBinding binding;
    public App app;

    CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setResources(root);
        return root;
    }

    private void setResources(View root) {
        setCategoryRecycler(root);
        disposable.add(app.getCookBookService().getApi().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<com.example.cookbook.api.models.Category>, Throwable>() {
                    @Override
                    public void accept(List<com.example.cookbook.api.models.Category> dates, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                        } else {
                            categoryAdapter.setCategories(dates);
                        }
                    }
                }));
    }

    private void setCategoryRecycler(View root) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        categoryRecycler = root.findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(manager);

        categoryAdapter = new CategoryAdapter(context);
        categoryRecycler.setAdapter(categoryAdapter);
    }


    @Override
    public void onDestroyView() {
        disposable.dispose();
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