package com.example.cookbook.api;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CookBookService {
    public static String TOKEN;
//    public static String BASE_URL = "http://10.0.2.2:8000";
    public static String BASE_URL = "http://192.168.0.3:8000";

    private Retrofit retrofit;
    CoocBookApi api;

    public CookBookService() {
        retrofit = createRetrofit();
        api = retrofit.create(CoocBookApi.class);
    }

    public CoocBookApi getApi() {
        return api;
    }

    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                final Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", TOKEN)
                        .build();
                return chain.proceed(request);
            }
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    private OkHttpClient createDebugHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createDebugHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void setAuthenticateOptions(){
        retrofit = retrofit.newBuilder()
                .client(createOkHttpClient())
                .build();
        api = retrofit.create(CoocBookApi.class);
    }

    public void setDefaultOptions(){
        retrofit = retrofit.newBuilder()
                .client(createDebugHttpClient())
//                .client(new OkHttpClient.Builder().build())
                .build();
        api = retrofit.create(CoocBookApi.class);
    }

    public void setTOKEN(String token){
        TOKEN = token;
    }
}
