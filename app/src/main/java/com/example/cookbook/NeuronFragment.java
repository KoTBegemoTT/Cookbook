package com.example.cookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.cookbook.adapters.FoundedIngredientAdapter;
import com.example.cookbook.adapters.IngredientAdapter;
import com.example.cookbook.databinding.FragmentNeuronBinding;

import java.io.File;
import java.io.IOException;


public class NeuronFragment extends Fragment {

    private Context context;
    private FragmentNeuronBinding binding;
    public App app;
    ImageView selectedImage;
    TextView foundedText;

    private RecyclerView foundedIngredientRecycler;
    private FoundedIngredientAdapter foundedIngredientAdapter;
    Button sendRequestButton;

    CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNeuronBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((Button) root.findViewById(R.id.selectImage))
                .setOnClickListener(btnChoosePhotoPressed);
        selectedImage = root.findViewById(R.id.selectedImage);
        sendRequestButton = root.findViewById(R.id.sendImageRequest);
        foundedText = root.findViewById(R.id.founded_text);

        foundedIngredientRecycler = root.findViewById(R.id.founded_ingredient_recycler);
        return root;
    }

    private void setFoundedIngredientRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        foundedIngredientRecycler.setLayoutManager(layoutManager);

        foundedIngredientAdapter = new FoundedIngredientAdapter(context);
        foundedIngredientRecycler.setAdapter(foundedIngredientAdapter);
    }

    public View.OnClickListener btnChoosePhotoPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            final int ACTIVITY_SELECT_IMAGE = 1234;
            ((Activity)context).startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
        }
    };

    public void onImageSelected(String imagePath) {
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        selectedImage.setImageBitmap(image);


        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(imagePath);
            }
        });
        sendRequestButton.setEnabled(true);

        foundedText.setVisibility(View.INVISIBLE);
        setFoundedIngredientRecycler();
    }


    protected void sendRequest(String path) {

        File file;
        file = new File(path);
        file.mkdirs();
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        Call<ResponseBody> call = app.getCookBookService().getApi().getNeiron(filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("T", "-------------------------------");
                try {
//                    Log.i("T", response.body().string());

                    String[] ing;
                    ing = response.body().string().replace("\"", "").split("&&");
                    foundedIngredientAdapter.setIngredients(ing);
                    foundedText.setVisibility(View.VISIBLE);
                    for (int i=0; i<ing.length; i++)
                        Log.i("T", ing[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("T", "++++++++++++++++++++++++++++++++++++++++++++++++++++T");
                Log.i("T", t.getMessage());
            }
        });
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