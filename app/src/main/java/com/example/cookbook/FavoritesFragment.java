package com.example.cookbook;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.adapters.PreviewAdapter;
import com.example.cookbook.api.models.Category;
import com.example.cookbook.api.models.Preview;
import com.example.cookbook.databinding.FragmentFavoritesBinding;
import com.example.cookbook.models.Dish;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;


public class FavoritesFragment extends Fragment {

    RecyclerView dishRecycler;
    PreviewAdapter previewAdapter;

    private Context context;
    private FragmentFavoritesBinding binding;
    public App app;

    CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setResources(root);
        return root;
    }

    private void setResources(View root) {
        setDishRecycler(root);
        disposable.add(app.getCookBookService().getApi().getLoved()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<Preview>, Throwable>() {
                    @Override
                    public void accept(List<Preview> dates, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                        } else {
                            previewAdapter.setPreviews(dates);
                        }
                    }
                }));

    }

    private void setDishRecycler(View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        dishRecycler = root.findViewById(R.id.dishRecycler);
        dishRecycler.setLayoutManager(layoutManager);

        previewAdapter = new PreviewAdapter(context, R.layout.dish_item_with_cross);
        dishRecycler.setAdapter(previewAdapter);
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