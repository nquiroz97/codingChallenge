package com.datechnologies.androidtest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.datechnologies.androidtest.views.AnimationActivity;
import com.datechnologies.androidtest.views.ChatActivity;
import com.datechnologies.androidtest.views.LoginActivity;

/**
 * The main screen that lets you navigate to all other screens in the app.
 *
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_main_title);
        setContentView(R.layout.activity_main);


        //DONE: Make the UI look like it does in the mock-up
        //DONE: Add a ripple effect when the buttons are clicked
    }

    //==============================================================================================
    // Button Click Methods
    //==============================================================================================

    public void onChatClicked(View v)
    {
        ChatActivity.start(this);
    }

    public void onLoginClicked(View v)
    {
        LoginActivity.start(this);
    }

    public void onAnimationClicked(View v)
    {
        AnimationActivity.start(this);
    }
}
