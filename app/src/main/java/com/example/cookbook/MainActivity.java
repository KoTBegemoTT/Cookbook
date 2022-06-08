package com.example.cookbook;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.TTS.SpeechToText;
import com.example.cookbook.api.models.api_classes.LoginResponse;
import com.example.cookbook.api.models.api_classes.User;
import com.example.cookbook.models.LoginDialog;
import com.example.cookbook.models.RegistrationDialog;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.example.cookbook.databinding.ActivityMainBinding;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SpeechToText.RecognizerCommandListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private LoginDialog loginDialog;
    private RegistrationDialog registrationDialog;
    private MenuItem enterAccount;

    private ImageButton openChronometer;
    private Chronometer chronometer;
    private AnimatorSet hide, show;

    public SpeechToText stt;
    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    public App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recipes, R.id.nav_favorites, R.id.nav_preparing, R.id.nav_timer,
                R.id.nav_options, R.id.nav_categories, R.id.recipe, R.id.category, R.id.nav_neuron)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ScaleMenuButton(binding.appBarMain.toolbar);

        enterAccount = binding.navView.getMenu().findItem(R.id.nav_menu_enter);
        setDialogs();
        setEnterButton();
        setChronometer();

        stt = new SpeechToText(this);
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        else
            stt.onRequestPermissionsGranted();
    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("T", "----------Result---------");

        switch(requestCode) {
            case 1234:
                Log.i("T", "----------case---------");
                if(resultCode == RESULT_OK){
                    Log.i("T", "----------OK---------");

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                    Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                    if (currentFragment != null && currentFragment instanceof NeuronFragment) {
                        Log.i("T", "----------FragmentNotNull---------");
                        ((NeuronFragment)currentFragment).onImageSelected(filePath);
                    }
                }
        }
    };

    private void setChronometer() {
        chronometer = findViewById(R.id.small_chronometer);
        chronometer.setCountDown(true);

        openChronometer = findViewById(R.id.open_chronometer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = chronometer.getBase() - SystemClock.elapsedRealtime();
                if (elapsedMillis <= 1) {
                    chronometer.stop();
                }
            }
        });

        chronometer.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        setOpenButton();
    }

    private void setOpenButton(){
        openChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textWidth = chronometer.getMeasuredWidth();

                ObjectAnimator closeText = ObjectAnimator.ofFloat(chronometer, "translationX", -textWidth);
                ObjectAnimator closeButton = ObjectAnimator.ofFloat(openChronometer, "translationX", -textWidth);
                ObjectAnimator rotateCloseButton = ObjectAnimator.ofFloat(openChronometer, "rotation", +180f);

                show = new AnimatorSet();
                show.playTogether(closeText, closeButton, rotateCloseButton);
                show.setDuration(500);
                show.start();
                setCloseButton();
            }
        });
    }

    void setCloseButton(){
        openChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textWidth = chronometer.getMeasuredWidth();

                ObjectAnimator closeText = ObjectAnimator.ofFloat(chronometer, "translationX", 0f);
                ObjectAnimator closeButton = ObjectAnimator.ofFloat(openChronometer, "translationX", 0f);
                ObjectAnimator rotateCloseButton = ObjectAnimator.ofFloat(openChronometer, "rotation", 0f);

                hide = new AnimatorSet();
                hide.playTogether(closeText, closeButton, rotateCloseButton);
                hide.setDuration(500);
                hide.start();
                setOpenButton();
            }
        });
    }

    private void setDialogs() {
        loginDialog = new LoginDialog(this);
        registrationDialog = new RegistrationDialog(this);

        Button enter = loginDialog.getEnterButton();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = loginDialog.getName().getText().toString();
                String password = loginDialog.getPassword().getText().toString();
                loginRequest(new User(name, password));
            }
        });

        TextView registration = loginDialog.getRegistration();
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationDialog.clear();
                registrationDialog.getDialog().show();
                loginDialog.getDialog().hide();
            }
        });

        Button register = registrationDialog.getRegistrationButton();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registrationDialog.getName().getText().toString();
                String password = registrationDialog.getPassword().getText().toString();
                String confirmPassword = registrationDialog.getConfirmPassword().getText().toString();
                String email = registrationDialog.getEmail().getText().toString();
                if (password.equals(confirmPassword))
                    registrationRequest(new User(name, password));
                else
                    Toast.makeText(getApplicationContext(), "Пароли должны совпадать", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginRequest(User user){
        app.getCookBookService().getApi().getToken(user)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.code() == 200){
                            enterAccount.setIcon(R.drawable.ic_menu_exit);
                            enterAccount.setTitle("Выйти из аккаунта");
                            setExitButton();
                            app.getCookBookService().setTOKEN("Token " + response.body().token);
                            app.getCookBookService().setAuthenticateOptions();
                            loginDialog.getDialog().hide();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                    }
                });
    }

    private void registrationRequest(User user){
        app.getCookBookService().getApi().registerUser(user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 201){
                            registrationDialog.getDialog().hide();
                            loginRequest(new User(user.username, user.password));

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    private void setExitButton() {
        enterAccount.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                enterAccount.setIcon(R.drawable.ic_menu_enter);
                enterAccount.setTitle("Войти в аккаунт");
                setEnterButton();
                app.getCookBookService().setDefaultOptions();
                return false;
            }
        });
    }

    private void setEnterButton() {
        enterAccount.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                loginDialog.clear();
                loginDialog.getDialog().show();
                return false;
            }
        });
    }


    public void ScaleMenuButton(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
//          -1 is id menu button
            if (child instanceof ImageButton && child.getId() == -1){
                child.setScaleX(1.4f);
                child.setScaleY(1.4f);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                stt.onRequestPermissionsGranted();
            } else {
                this.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stt.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public App getCookApplication(){
        return app;
    }

    @Override
    public void onGetCommand(String command) {
        Log.i("T", "----------onGetCommand---------");
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (currentFragment != null && currentFragment instanceof RecipeFragment) {
            Log.i("T", "----------FragmentNotNull---------");
            ((RecipeFragment)currentFragment).executeCommand(command);
        }
    }
}