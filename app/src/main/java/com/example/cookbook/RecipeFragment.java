package com.example.cookbook;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cookbook.adapters.IngredientAdapter;
import com.example.cookbook.adapters.RecipeStepAdapter;
import com.example.cookbook.api.CookBookService;
import com.example.cookbook.api.models.Dish;
import com.example.cookbook.api.models.api_classes.Relation;
import com.example.cookbook.api.models.api_classes.UserDishRequest;
import com.example.cookbook.databinding.FragmentRecipeBinding;
import com.example.cookbook.models.RecipeStep;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeFragment extends Fragment implements TextToSpeech.OnInitListener {

    String dishID;

    private Context context;
    private FragmentRecipeBinding binding;
    private RecyclerView ingredientRecycler;
    private RecyclerView stepRecycler;
    private IngredientAdapter ingredientAdapter;
    private RecipeStepAdapter stepAdapter;
    public App app;
    private Dialog dialog;

    Relation relation = null;
    boolean hasRelation;
    Button changeLovedStatusButton;
    RatingBar ratingBar;
    TextView rating;

    public TextToSpeech tts;

    Map<String, Integer> stringNumbersToInt = new HashMap<String, Integer>(){{
        put("один", 1);
        put("два", 2);
        put("три", 3);
        put("четыре", 4);
        put("пять", 5);
        put("шесть", 6);
        put("семь", 7);
        put("восемь", 8);
        put("девять", 9);
        put("десять", 10);
    }};

    CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = this.getArguments();
        dishID = bundle.getString("DISH_ID");

        changeLovedStatusButton = root.findViewById(R.id.add_to_loved);

        setResources(root);

        ((MainActivity) getActivity()).stt.onStartFragment();

        tts = new TextToSpeech(getActivity(), this);

        return root;
    }

    private void setResources(View root) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.rate_window);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RatingBar rating = root.findViewById(R.id.dishRatingBar);
        rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    dialog.show();
                }
                return true;
            }
        });

        ImageButton closeButton = dialog.findViewById(R.id.close_change_field);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingBar ratingBar = dialog.findViewById(R.id.rate);
                String rate = ratingBar.getRating()+"";
                if (relation == null){
                    relation = new Relation(rate);
                    SetRelation(false, true);
                }
                else{
                    relation.rate = rate;
                    updateRelation(false, true);
                }
                dialog.hide();
            }
        });


        setIngredientRecycler(root);
        disposable.add(app.getCookBookService().getApi().getDish(dishID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Dish, Throwable>() {
                    @Override
                    public void accept(Dish dish, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                        } else {
                            setDish(dish, root);
                            String[] ingredients = dish.getIngredients_name().split("&");
                            String[] amount = dish.getIngredients_number().split("&");
                            ingredientAdapter.setIngredients(ingredients, amount);
                        }
                    }
                }));

//        TextView dishSteps = root.findViewById(R.id.steps);
        setStepRecycler(root);
        disposable.add(app.getCookBookService().getApi().getStepsOfDish(dishID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<RecipeStep>, Throwable>() {
                    @Override
                    public void accept(List<RecipeStep> steps, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                        } else {
//                            String stepsss = "";
//                            for(int i=0; i < steps.size(); i++)
//                                stepsss = stepsss + steps.get(i).getStepDescription() + " : " + steps.get(i).getStepImage() + "\n\n";
//                            dishSteps.setText(stepsss);
                            stepAdapter.setSteps(steps);
                        }
                    }
                }));

        disposable.add(app.getCookBookService().getApi().getRelation(dishID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Relation, Throwable>() {
                    @Override
                    public void accept(Relation rel, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                            hasRelation = false;
                        } else {
                            relation = new Relation(rel);
                            hasRelation = true;
                            if (relation.loved) {
                                changeLovedStatusButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                        R.drawable.loved_true, 0);
                                changeLovedStatusButton.setText("Удалить из избранного");
                            }
                        }
                    }
                }));

        changeLovedStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeLovedStatus();
            }
        });

    }

    private void setDish(Dish dish, View root){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        TextView title = root.findViewById(R.id.title);
        title.setText(dish.getName());
        ImageView image = root.findViewById(R.id.recipeImage);
        String imageUrl = CookBookService.BASE_URL + dish.getImageUrl();
        ImageLoader.getInstance().displayImage(imageUrl, image);
        String cal = dish.getCalorie();
        TextView calorie = root.findViewById(R.id.calorie_content);
        if (cal.equals("?")){
            ImageView calorieBg = root.findViewById(R.id.calorie_background);
            calorieBg.setVisibility(View.INVISIBLE);
            calorie.setText("");
        }
        else
            calorie.setText(cal);
        ratingBar = root.findViewById(R.id.dishRatingBar);
        ratingBar.setRating(Float.parseFloat(dish.getRating()));
        rating = root.findViewById(R.id.rating);
        rating.setText(dish.getRating() + "(" + dish.getVote() + ")");
        TextView description = root.findViewById(R.id.description_text);
        description.setText(dish.getDescription());
        TextView source = root.findViewById(R.id.source);
        source.setText("Источник: " + dish.getSource());
//        TextView recipe = root.findViewById(R.id.recipe);
//        recipe.setText(dish.getRecipe());
    }

    private void setIngredientRecycler(View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        ingredientRecycler = root.findViewById(R.id.ingredient_recycler);
        ingredientRecycler.setLayoutManager(layoutManager);

        ingredientAdapter = new IngredientAdapter(context);
        ingredientRecycler.setAdapter(ingredientAdapter);
    }

    private void setStepRecycler(View root) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        stepRecycler = root.findViewById(R.id.steps_recycler);
        stepRecycler.setLayoutManager(layoutManager);

        stepAdapter = new RecipeStepAdapter(context);
        stepRecycler.setAdapter(stepAdapter);
    }

    public void ChangeLovedStatus(){
        if (relation == null){
            relation = new Relation(true);
            SetRelation(true, false);
            hasRelation = true;
        }
        else{
            relation.loved = !relation.loved;
            updateRelation(true, false);
        }
    }

    public void SetLovedButton(){
        if (relation.loved){
            changeLovedStatusButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.loved_true, 0);
            changeLovedStatusButton.setText("Удалить из избранного");
        }
        else {
            changeLovedStatusButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.loved_false, 0);
            changeLovedStatusButton.setText("Добавить в избранное");
        }
    }

    public void updateRating(){
        disposable.add(app.getCookBookService().getApi().getDish(dishID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Dish, Throwable>() {
                    @Override
                    public void accept(Dish dish, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Log.i("T", throwable.getMessage());
                        } else {
                            ratingBar.setRating(Float.parseFloat(dish.getRating()));
                            rating.setText(dish.getRating() + "(" + dish.getVote() + ")");
                        }
                    }
                }));
    }

    public void SetRelation(boolean changeLoved, boolean changeRate){
        app.getCookBookService().getApi().setRelation(dishID, new UserDishRequest(relation))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (changeLoved)
                            SetLovedButton();
                        if (changeRate)
                            updateRating();
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    public void updateRelation(boolean changeLoved, boolean changeRate){
        app.getCookBookService().getApi().updateRelation(dishID, new UserDishRequest(relation))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200)
                            if (changeLoved)
                                SetLovedButton();
                            if (changeRate)
                                updateRating();
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }


    @Override
    public void onDestroyView() {
        disposable.dispose();
        super.onDestroyView();
        binding = null;

        ((MainActivity) getActivity()).stt.onDestroyFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        app = (App) activity.getApplication();
    }

    public void executeCommand(String command){
        Log.i("T", "----------executeCommand---------");
        String[] words = command.split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        switch (firstWord){
            case ("перейди"):
                if(lastWord.equals("ингредиентам"))
                    {
                        scrollToIngredients();
                    }
                else
                    scrollToStep(stringNumbersToInt.get(lastWord));
                break;
            case ("озвуч"):
                if(lastWord.equals("ингредиенты"))
                    repitIngredients();
                else
                    repitStep(stringNumbersToInt.get(lastWord));
                break;
        }
    }

    public void scrollToStep(int step){
        Log.i("T", "SCROLLLLLLLLLLLLLLLLLLLL");
        final float y = stepRecycler.getY() + stepRecycler.getChildAt(step - 1).getY();
        NestedScrollView NsV = getActivity().findViewById(R.id.recipe_scroller);
//        Toast.makeText(context, y + "", Toast.LENGTH_SHORT).show();
        NsV.smoothScrollTo(0, (int) y - 20);
    }

    public void scrollToIngredients(){
        Log.i("T", "SCROLLLLLLLLLLLLLLLLLLLL");
        final float y = getActivity().findViewById(R.id.ingredients).getY();
        NestedScrollView NsV = getActivity().findViewById(R.id.recipe_scroller);
//        Toast.makeText(context, y + "", Toast.LENGTH_SHORT).show();
        NsV.smoothScrollTo(0, (int) y - 20);
    }

    public void repitStep(int step){
        String stepDescription = stepAdapter.getSteps().get(step - 1).getStepDescription();
        sayIt(stepDescription);
    }

    public void repitIngredients(){
        String ingredientsAmount = ingredientAdapter.getIngredientAmounts();
        sayIt(ingredientsAmount);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(new Locale(Locale.getDefault().getLanguage()));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                Toast.makeText(getActivity(), "NOT SUPPORTED", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getActivity(), "INICIALIZATION FAILED", Toast.LENGTH_SHORT).show();
    }

    public void sayIt(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
    }
}