package com.example.cookbook;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.adapters.PreviewAdapter;
import com.example.cookbook.databinding.FragmentPreparingBinding;
import com.example.cookbook.models.Dish;

import java.util.ArrayList;
import java.util.List;


public class PreparingFragment extends Fragment {

    RecyclerView dishRecycler;
    PreviewAdapter previewAdapter;

    private Context context;
    private FragmentPreparingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreparingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setResources(root);
        return root;
    }

    private void setResources(View root) {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("borsch", "Борщ со свининой", "12", "8",  4f));
        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
        dishes.add(new Dish("borsch", "Борщ со свининой", "12", "8",  4f));
        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));
        dishes.add(new Dish("desert", "Фруктовый десерт с кремом из маскарпоне", "7", "4",  3.5f));

        setDishRecycler(dishes, root);
    }

    private void setDishRecycler(List<Dish> dishes, View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        dishRecycler = root.findViewById(R.id.dishRecycler);
        dishRecycler.setLayoutManager(layoutManager);

//        previewAdapter = new PreviewAdapter(context, dishes, R.layout.dish_item_with_cross);
        dishRecycler.setAdapter(previewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

}