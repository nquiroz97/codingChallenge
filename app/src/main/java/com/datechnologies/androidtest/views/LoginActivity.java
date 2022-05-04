package com.datechnologies.androidtest.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        activityLoginBinding.setLifecycleOwner(this);
        activityLoginBinding.setLoginActivityViewModel(loginViewModel);

        Button loginButton = activityLoginBinding.idBtnLogin;

        loginViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        loginViewModel.getLoginResult().observe(this, s -> showLoginResponseDialog(s, loginViewModel.isSuccess));

        loginButton.setOnClickListener(view -> {
            if(validateLogin(loginViewModel.emailAddress, loginViewModel.password)){
                loginViewModel.login(loginViewModel.emailAddress, loginViewModel.password);
            }
        });
    }

    private boolean validateLogin(String username, String password){
        boolean usernameIsNull =  username == null;
        boolean passwordIsNull = password == null;
        boolean usernameIsEmpty = username.trim().length() == 0;
        boolean passwordIsEmpty = username.trim().length() == 0;

        if((usernameIsNull || usernameIsEmpty) && (passwordIsNull || passwordIsEmpty)){
            showErrorToast(LoginError.BOTH);
            return false;
        }
        if(usernameIsNull || usernameIsEmpty){
            showErrorToast(LoginError.EMAIL);
            return false;
        }
        if(password == null || password.trim().length() == 0){
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
        FragmentManager fm = getSupportFragmentManager();
        LoginResponseDialog loginResponseDialog = LoginResponseDialog.newInstance(displayText, successfulLogin);
        loginResponseDialog.show(fm, "fragment_login");
    }
}
