package com.datechnologies.androidtest.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;

/**
 * Screen that displays the D & A Technologies logo.
 * The icon can be moved around on the screen as well as animated.
 * */

public class AnimationActivity extends AppCompatActivity {

    //==============================================================================================
    // Class Properties
    //==============================================================================================
    private ImageView imageView;
    private Button button;
    private float startingX,  startingY;
    private Animation fadeOutIn;


    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, AnimationActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        imageView = findViewById(R.id.draggableImage);

        imageView.setOnTouchListener(this::handleOnTouch);

        fadeOutIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_in);

        button = findViewById(R.id.idBtnAnimation);
        button.setOnClickListener(view -> imageView.startAnimation(fadeOutIn));

        // DONE: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // DONE: Add a ripple effect when the buttons are clicked

        // DONE: When the fade button is clicked, you must animate the D & A Technologies logo.
        // DONE: It should fade from 100% alpha to 0% alpha, and then from 0% alpha to 100% alpha

        // DONE: The user should be able to touch and drag the D & A Technologies logo around the screen.

        // TODO: Add a bonus to make yourself stick out. Music, color, fireworks, explosions!!!
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean handleOnTouch(View view, @NonNull MotionEvent motionEvent){
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                // Remember where we started (for dragging)
                startingX = motionEvent.getX();
                startingY = motionEvent.getY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float movedX = motionEvent.getX();
                final float movedY = motionEvent.getY();

                // Calculate the distance moved
                final float distanceX = movedX - startingX;
                final float distanceY = movedY - startingY;

                imageView.setX(imageView.getX() + distanceX);
                imageView.setY(imageView.getY() + distanceY);

                view.invalidate();
                break;
            }
        }
        return true;
    }
}
