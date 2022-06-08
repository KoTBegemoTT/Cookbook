package com.example.cookbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cookbook.databinding.FragmentTimerBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class TimerFragment extends Fragment {

    private Chronometer mChronometer, smallChronometer;
    private int timerСontent = 0;
    private boolean isTimerActive = false;
    private long timeLeftUntilPause = 0;

    ImageButton playPause, openChronometer;

    private Context context;
    private FragmentTimerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mChronometer = root.findViewById(R.id.chronometer);
        mChronometer.setCountDown(true);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setText("00:00:00");
        mChronometer.setOnChronometerTickListener(ChronometerTickListener);

        timerСontent = 0;

        setButtons(root);

        smallChronometer = getParentFragment().getActivity().findViewById(R.id.small_chronometer);
        openChronometer = getParentFragment().getActivity().findViewById(R.id.open_chronometer);
        Pause(smallChronometer);
        Play();
        hideSmallChronometer();
        return root;
    }

    private void hideSmallChronometer() {
        smallChronometer.setVisibility(View.INVISIBLE);
        openChronometer.setVisibility(View.INVISIBLE);
    }

    private void showSmallChronometer() {
        smallChronometer.setVisibility(View.VISIBLE);
        openChronometer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (isTimerActive){
            showSmallChronometer();
            long timeLeft = mChronometer.getBase() - SystemClock.elapsedRealtime();
            smallChronometer.setBase(SystemClock.elapsedRealtime() + timeLeft);
            smallChronometer.start();

            smallChronometer.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            openChronometer.performClick();
            openChronometer.performClick();
        }
        else{
            smallChronometer.setBase(SystemClock.elapsedRealtime());
            smallChronometer.stop();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    private void setButtons(View root) {
        playPause = root.findViewById(R.id.button_play_pause);
        playPause.setOnClickListener(playPauseButton);

        ImageButton restart = root.findViewById(R.id.button_restart);
        restart.setOnClickListener(ResetClickListener);

        ImageButton erase = root.findViewById(R.id.button_erase);
        erase.setOnClickListener(EraseClickListener);

        int[] buttonIndexes = {R.id.number0, R.id.number1, R.id.number2, R.id.number3, R.id.number4,
                R.id.number5, R.id.number6, R.id.number7, R.id.number8, R.id.number9};

        for (int index : buttonIndexes) {
            root.findViewById(index).setOnClickListener(NumberClickListener);
        }
    }

    Chronometer.OnChronometerTickListener ChronometerTickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            long elapsedMillis = chronometer.getBase() - SystemClock.elapsedRealtime();
            int h = (int) (elapsedMillis / 3600000);
            int m = (int) (elapsedMillis - h * 3600000) / 60000;
            int s = (int) (elapsedMillis - h * 3600000 - m * 60000) / 1000;
            String hh = h < 10 ? "0" + h : h + "";
            String mm = m < 10 ? "0" + m : m + "";
            String ss = s < 10 ? "0" + s : s + "";
            chronometer.setText(hh + ":" + mm + ":" + ss);
            if (elapsedMillis <= 1) {
                chronometer.stop();
                timerСontent = 0;
                isTimerActive = false;
                playPause.setImageResource(R.drawable.ic_play);
            }
        }
    };

    View.OnClickListener playPauseButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isTimerActive) {
                Pause(mChronometer);
                return;
            }
            Play();
        }
    };

    public void Play() {
        if (timerСontent <= 0)
            return;
        if (timeLeftUntilPause > 0) {
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeLeftUntilPause);
            mChronometer.start();
            isTimerActive = true;
            playPause.setImageResource(R.drawable.ic_pause);
            timeLeftUntilPause = 0;
            return;
        }
        int seconds = getSeconds();
        mChronometer.setBase(SystemClock.elapsedRealtime() + 1000 * seconds);
        mChronometer.getOnChronometerTickListener().onChronometerTick(mChronometer);
        mChronometer.start();
        isTimerActive = true;
        playPause.setImageResource(R.drawable.ic_pause);
    }

    public int getSeconds() {
        if (timerСontent > 995959)
            timerСontent = 995959;
        int h = timerСontent / 10000;
        int m = (timerСontent % 10000) / 100;
        int s = timerСontent % 100;
        return h * 3600 + m * 60 + s;
    }

    public void Pause(Chronometer chronometer) {
        timeLeftUntilPause = chronometer.getBase() - SystemClock.elapsedRealtime();
        if (timeLeftUntilPause <0)
            timeLeftUntilPause = 0;
        timerСontent = millisecondsToTimerContent(timeLeftUntilPause);
        chronometer.stop();
        isTimerActive = false;
        playPause.setImageResource(R.drawable.ic_play);
    }

    private int millisecondsToTimerContent(long milis) {
        int h = (int) (milis / 3600000);
        int m = (int) (milis - h * 3600000) / 60000;
        int s = (int) (milis - h * 3600000 - m * 60000) / 1000;
        return h * 10000 + m * 100 + s;
    }

    View.OnClickListener ResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            timerСontent = 0;
            mChronometer.stop();
            mChronometer.setText("00:00:00");
            timeLeftUntilPause = 0;
            isTimerActive = false;
            playPause.setImageResource(R.drawable.ic_play);
        }
    };

    View.OnClickListener NumberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isTimerActive || timerСontent > 99999)
                return;
            switch (view.getId()) {
                case (R.id.number0):
                    timerСontent = timerСontent * 10;
                    break;
                case (R.id.number1):
                    timerСontent = timerСontent * 10 + 1;
                    break;
                case (R.id.number2):
                    timerСontent = timerСontent * 10 + 2;
                    break;
                case (R.id.number3):
                    timerСontent = timerСontent * 10 + 3;
                    break;
                case (R.id.number4):
                    timerСontent = timerСontent * 10 + 4;
                    break;
                case (R.id.number5):
                    timerСontent = timerСontent * 10 + 5;
                    break;
                case (R.id.number6):
                    timerСontent = timerСontent * 10 + 6;
                    break;
                case (R.id.number7):
                    timerСontent = timerСontent * 10 + 7;
                    break;
                case (R.id.number8):
                    timerСontent = timerСontent * 10 + 8;
                    break;
                case (R.id.number9):
                    timerСontent = timerСontent * 10 + 9;
                    break;
            }
            timeLeftUntilPause = 0;
            String timerText = convertTimerToString(timerСontent);
            mChronometer.setText(timerText);
        }
    };

    private String convertTimerToString(int timer) {
        int h = timer / 10000;
        int m = (timer % 10000) / 100;
        int s = timer % 100;
        String hh = h < 10 ? "0" + h : h + "";
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";
        return hh + ":" + mm + ":" + ss;
    }


    View.OnClickListener EraseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isTimerActive)
                return;
            timerСontent = timerСontent / 10;
            String timerText = convertTimerToString(timerСontent);
            mChronometer.setText(timerText);
            timeLeftUntilPause = 0;
        }
    };
}