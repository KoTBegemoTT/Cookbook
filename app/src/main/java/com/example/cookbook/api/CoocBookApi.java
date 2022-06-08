package com.example.cookbook.api;


import com.example.cookbook.api.models.Category;
import com.example.cookbook.api.models.Dish;
import com.example.cookbook.api.models.NamesDTO;
import com.example.cookbook.api.models.Preview;
import com.example.cookbook.api.models.api_classes.LoginResponse;
import com.example.cookbook.api.models.api_classes.User;
import com.example.cookbook.api.models.api_classes.Relation;
import com.example.cookbook.api.models.api_classes.UserDishRequest;
import com.example.cookbook.models.RecipeStep;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CoocBookApi {

    @GET("api/test_names")
    Single<List<NamesDTO>> getNames();

    @GET("api/category")
    Single<List<Category>> getCategories();

    @GET("api/popular")
    Single<List<Preview>> getPreviews();

    @GET("api/preview/{category}")
    Single<List<Preview>> getDishOnCategory(@Path("category") String cat);

    @GET("api/dish/{dish}")
    Single<Dish> getDish(@Path("dish") String date);

    @GET("api/stepsOfDish/{dish}")
    Single<List<RecipeStep>> getStepsOfDish(@Path("dish") String steps);

    @POST("auth/users/")
    Call<Void> registerUser(@Body User registrationBody);

    @POST("auth/token")
    Call<LoginResponse> getToken(@Body User registrationBody);

    @GET("api/user-dish/{dish}")
    Single<Relation> getRelation(@Path("dish") String date);

    @POST("api/user-dish/{dish}")
    Call<Void> setRelation(@Path("dish") String date, @Body UserDishRequest relation);

    @PUT("api/user-dish/{dish}")
    Call<Void> updateRelation(@Path("dish") String date, @Body UserDishRequest relation);

    @GET("api/user-dish/loved")
    Single<List<Preview>> getLoved();

    @POST("api/neiron")
    @Multipart
    Call<ResponseBody> getNeiron(@Part MultipartBody.Part body);
}
