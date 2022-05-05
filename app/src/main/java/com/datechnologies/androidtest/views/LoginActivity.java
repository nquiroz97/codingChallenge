package com.datechnologies.androidtest.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.databinding.ActivityLoginBinding;
import com.datechnologies.androidtest.enums.LoginError;
import com.datechnologies.androidtest.viewmodels.LoginActivityViewModel;

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 *
 */

public class LoginActivity extends CommonActivity{

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================
    LoginActivityViewModel loginViewModel;
    private ProgressBar loadingIndicator;


    public static void start(Context context)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();

        // DONE: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // DONE: Add a ripple effect when the buttons are clicked
        // DONE: Save screen state on screen rotation, inputted username and password should not disappear on screen rotation
        // DONE: Send 'email' and 'password' to http://dev.rapptrlabs.com/Tests/scripts/login.php
        // DONE: as FormUrlEncoded parameters.
        // DONE: When you receive a response from the login endpoint, display an AlertDialog.
        // DONE: The AlertDialog should display the 'code' and 'message' that was returned by the endpoint.
        // DONE: The AlertDialog should also display how long the API call took in milliseconds.
        // DONE: When a login is successful, tapping 'OK' on the AlertDialog should bring us back to the MainActivity
        // DONE: The only valid login credentials are:
        // DONE: email: info@rapptrlabs.com
        // DONE: password: Test123
        // DONE: so please use those to test the login.
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setupUI(){
        //called from Common Activity
        setupActionBar();

        loginViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        activityLoginBinding.setLifecycleOwner(this);
        activityLoginBinding.setLoginActivityViewModel(loginViewModel);

        Button loginButton = activityLoginBinding.idBtnLogin;
        loadingIndicator = activityLoginBinding.loadingIndicator;

        loginViewModel.getLoginResult().observe(this, s -> showLoginResponseDialog(s, loginViewModel.isSuccess));

        loginButton.setOnClickListener(view -> {
            if(validateLogin(loginViewModel.emailAddress, loginViewModel.password)){
                startLoginProcess(loginViewModel.emailAddress, loginViewModel.password);
            }
        });
    }

    private void startLoginProcess(String email, String password){
        loadingIndicator.setVisibility(View.VISIBLE);
        disableUserInteraction();
        loginViewModel.login(email, password);
    }

    private boolean validateLogin(String email, String password){
        boolean emailIsNull =  email == null, passwordIsNull = password == null;
        boolean emailIsEmpty = (email != null ? email.trim().length() : 1) == 0;
        boolean passwordIsEmpty = (password != null ? password.trim().length() : 1) == 0;

        if((emailIsNull || emailIsEmpty) && (passwordIsNull || passwordIsEmpty)){
            showErrorToast(LoginError.BOTH);
            return false;
        }
        if(emailIsNull || emailIsEmpty){
            showErrorToast(LoginError.EMAIL);
            return false;
        }
        if(passwordIsNull || passwordIsEmpty){
            showErrorToast(LoginError.PASSWORD);
            return false;
        }
        return true;
    }

    private void showErrorToast(LoginError loginError){
        String message;
        switch (loginError){
            case EMAIL:
                message = "Email is required";
                break;
            case PASSWORD:
                message = "Password is required";
                break;
            case BOTH:
                message = "Both email and password are required";
                break;
            default:
                message = "Something is wrong";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLoginResponseDialog(String displayText, boolean successfulLogin) {
        loadingIndicator.setVisibility(View.GONE);
        enableUserInteraction();
        FragmentManager fm = getSupportFragmentManager();
        LoginResponseDialog loginResponseDialog = LoginResponseDialog.newInstance(displayText, successfulLogin);
        loginResponseDialog.show(fm, "fragment_login");
    }

    private void disableUserInteraction(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void enableUserInteraction(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
