package com.example.cookbook;

import android.app.Application;

import com.example.cookbook.api.CookBookService;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    private CookBookService cookBookService;
    public boolean isAuthenticated;

    @Override
    public void onCreate() {
        super.onCreate();

        cookBookService = new CookBookService();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.img_empty)
                .showImageForEmptyUri(R.drawable.img_empty)
                .showImageOnFail(R.drawable.img_empty)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)

                .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public CookBookService getCookBookService() {
        return cookBookService;
    }
}
