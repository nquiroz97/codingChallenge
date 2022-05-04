package com.datechnologies.androidtest.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.datechnologies.androidtest.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }
}
